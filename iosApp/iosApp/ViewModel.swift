import shared
import FeedKit
import AVKit
import Combine

class CastawayViewModel: ObservableObject {
    
    private let getStoredFeedUseCase: NativeGetStoredFeedUseCase
    private let storeAndGetFeedUseCase: StoreAndGetFeedUseCase
    private let storeEpisodeUseCase: NativeSaveEpisodeUseCase
    private let loadImageUseCase: NativeLoadImageUseCase
    private let player = CastawayPlayer()
    
    private var disposables = Set<AnyCancellable>()
    
    @Published var feedTitle = ""
    @Published var feedImage: UIImage?
    @Published var episodes = [Episode]()
    @Published var currentEpisode: Episode?
    @Published var playing: Bool = false
    var playbackPosition: PlayerTimeObserver
    var playbackDuration: PlayerDurationObserver
    var playbackStatePublisher: CurrentValueSubject<PlaybackState, Never>
    
    init() {
        storeAndGetFeedUseCase = StoreAndGetFeedUseCase()
        storeEpisodeUseCase = NativeSaveEpisodeUseCase()
        getStoredFeedUseCase = NativeGetStoredFeedUseCase()
        loadImageUseCase = NativeLoadImageUseCase()
        playbackPosition = player.playbackTimeObserver
        playbackDuration = player.playbackDurationObserver
        playbackStatePublisher = player.playbackState
        
        observePlaybackState()
    }
    
    private func observePlaybackState() {
        playbackStatePublisher
            .sink(receiveValue: { state in
                self.storeEpisodeOnPausedOrStopped(state)
            })
            .store(in: &disposables)
    }
    
    private func storeEpisodeOnPausedOrStopped(_ state: PlaybackState) {
        if state == PlaybackState.paused || state == PlaybackState.stopped {
            guard let episode = currentEpisode?
                    .copy(playbackPosition: PlaybackPosition(
                        position: playbackPosition.publisher.value,
                        duration: playbackDuration.publisher.value
                    )) else { return }
            storeEpisode(episode: episode)
        }
    }
    
    func loadFeed(_ url: String) {
        getStoredFeedUseCase.run(
            url: url,
            onSuccess: { feed in
                print("Local ✅")
                self.publishAndPrepareFeed(feed)
            },
            onError: { error in
                print("There is no stored Feed: \(url) ❌ \(error) 👉 💾 Download...")
                self.fetchFeed(url)
            })
    }
    
    private func fetchFeed(_ url: String) {
        storeAndGetFeedUseCase.run(url: url) { result in
            switch result {
            case .success(let feed):
                print("Fetched 💯")
                self.publishAndPrepareFeed(feed)
            case .failure(let error):
                print(error)
            }
        }
    }
    
    func loadImage(_ url: String) {
        loadImageUseCase.run(
            url: url,
            onSuccess: { image in
                print("🖼 \(image)")
                self.feedImage = image
            },
            onError: { error in
                print("Fail to load Image: \(url) ❌ \(error)")
            })
    }
    
    private func publishAndPrepareFeed(_ feed: FeedData) {
        feedTitle = feed.title
        episodes = feed.episodes
        player.prepare(media: feed.episodes.map{ episode in episode.toMediaData() })
        
        if let imageUrl = feed.image {
            loadImage(imageUrl)
        }
    }
    
    func episodeClicked(episode: Episode, playState: Bool) {
        playPause(episode, playState, episode.playbackPosition.position)
    }
    
    private func playPause(_ episode: Episode, _ playState: Bool, _ startAt: Int64) {
        if currentEpisode?.id == episode.id {
            playPauseCurrent(playState: playState)
        } else {
            playPauseNew(playState, episode, startAt)
        }
    }
    
    func playPauseCurrent(playState: Bool) {
        guard currentEpisode != nil else { return }
        playing = playState
        if playing {
            player.resume()
        } else {
            player.pause()
        }
    }
    
    private func playPauseNew(_ playState: Bool, _ episode: Episode, _ startAt: Int64) {
        playing = playState
        if playing {
            player.play(mediaId: episode.id, startAt: startAt)
        }
        currentEpisode = episode
    }
    
    func forwardCurrentItem() {
        player.fastForward()
    }
    
    func replayCurrentItem() {
        player.rewind()
    }
    
    func skipToPrevious() {
        player.skipToPrevious()
    }
    
    func skipToNext() {
        player.skipToNext()
    }
    
    func seekTo(positionMillis: Int64) {
        player.seekTo(position: positionMillis)
    }
    
    func playbackSpeed(speed: Float) {
        player.speed(speed: speed)
    }
    
    private func playingState(mediaId: String) -> Bool {
        return false
    }
    
    private func storeEpisode(episode: Episode) {
        storeEpisodeUseCase.run(
            episode: episode,
            onSuccess: { storedEpisode in
                print("Stored: 💾 \(storedEpisode.title)")
                if let index = self.episodes.firstIndex(where: {$0.id == storedEpisode.id}) {
                    self.episodes[index] = storedEpisode
                }
            },
            onError: { error in
                print("Error storing: ❌ \(error)")
            })
    }
}
