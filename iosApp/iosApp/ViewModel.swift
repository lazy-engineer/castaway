import shared
import FeedKit
import AVKit
import Combine

class CastawayViewModel: ObservableObject {
    
    private let getStoredFeedUseCase: NativeGetStoredFeedUseCase
    private let storeAndGetFeedUseCase: StoreAndGetFeedUseCase
    private let storeEpisodeUseCase: NativeSaveEpisodeUseCase
    private let storedEpisodeFlowableUseCase: NativeStoredEpisodeFlowableUseCase
    private let player = CastawayPlayer()
    
    private var disposables = Set<AnyCancellable>()
    
    @Published var feedTitle = ""
    @Published var feedImage: UIImage?
    @Published var episodes = [Episode]()
    @Published var currentEpisode: Episode?
    @Published var playing: Bool = false
    @Published var playbackSpeed: Float = 1.0
    var playbackPosition: PlayerTimeObserver
    var playbackDuration: PlayerDurationObserver
    var playbackStatePublisher: CurrentValueSubject<PlaybackState, Never>
    var nowPlayingPublisher: CurrentValueSubject<String?, Never>
    
    init() {
        storeAndGetFeedUseCase = StoreAndGetFeedUseCase()
        storeEpisodeUseCase = NativeSaveEpisodeUseCase()
        getStoredFeedUseCase = NativeGetStoredFeedUseCase()
        storedEpisodeFlowableUseCase = NativeStoredEpisodeFlowableUseCase()
        playbackPosition = player.playbackTimeObserver
        playbackDuration = player.playbackDurationObserver
        playbackStatePublisher = player.playbackState
        nowPlayingPublisher = player.nowPlaying
        
        observePlaybackState()
    }
    
    private func observePlaybackState() {
        playbackStatePublisher
            .sink(receiveValue: { state in
                self.playing = state == PlaybackState.playing || state == PlaybackState.readyToPlay
                self.storeEpisodeOnPausedOrStopped(state)
            })
            .store(in: &disposables)
        
        nowPlayingPublisher
            .sink(receiveValue: { mediaId in
                guard let currentId = mediaId else { return }
                
                if let index = self.episodes.firstIndex(where: {$0.id == currentId}) {
                    self.currentEpisode = self.episodes[index]
                }
                self.playbackSpeed = 1.0
            })
            .store(in: &disposables)
    }
    
    private func storeEpisodeOnPausedOrStopped(_ state: PlaybackState) {
        if state == PlaybackState.paused || state == PlaybackState.stopped || state == PlaybackState.finished {
            storeCurrentUpdatedEpisode()
        }
    }
    
    private func storeCurrentUpdatedEpisode() {
        guard let episode = currentEpisode?
                .copy(playbackPosition: PlaybackPosition(
                    position: playbackPosition.publisher.value,
                    duration: playbackDuration.publisher.value
                )) else { return }
        storeEpisode(episode: episode)
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
    
    func loadEpisodes(_ feedUrl: String) {
        storedEpisodeFlowableUseCase.run(
            feedUrl: feedUrl,
            onEach: { episode in
                print("Episode \(episode.id) 💧")

                if self.episodes.firstIndex(where: {$0.id == episode.id}) == nil {
                    self.player.append(media: episode.toMediaData())
                    self.episodes.append(episode)
                    print("\(self.episodes.count) 💦")
                }
            },
            onError: { error in
                print("Failed to load episodes: ❌ \(error)")
            },
            onComplete: {})
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
    
    private func publishAndPrepareFeed(_ feed: FeedData) {
        feedTitle = feed.info.title
        episodes = feed.episodes
        feedImage = feed.info.image
        prepareEpisodes(feed.episodes)
    }
    
    private func prepareEpisodes(_ episodes: [Episode]) {
        player.prepare(media: episodes.map{ episode in episode.toMediaData() })
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
        if playState {
            player.resume()
        } else {
            player.pause()
        }
    }
    
    private func playPauseNew(_ playState: Bool, _ episode: Episode, _ startAt: Int64) {
        if playState {
            player.play(mediaId: episode.id, startAt: startAt)
        }
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
    
    func changePlaybackSpeed() {
        let supportedSpeedRates: [Float] = [1.0, 1.5, 2.0]
        guard let currentIndex = supportedSpeedRates.firstIndex(of: playbackSpeed) else { return }
        var newIndex = 0
        if supportedSpeedRates.count > currentIndex + 1 {
            newIndex = currentIndex + 1
        }
        
        playbackSpeed = supportedSpeedRates[newIndex]
        playbackSpeed(speed: playbackSpeed)
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
