import Foundation
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
    var playbackPositionPublisher: CurrentValueSubject<Int64, Never>
    var playbackDurationPublisher: CurrentValueSubject<Int64, Never>
    
    init() {
        self.storeAndGetFeedUseCase = StoreAndGetFeedUseCase()
        self.storeEpisodeUseCase = NativeSaveEpisodeUseCase()
        self.getStoredFeedUseCase = NativeGetStoredFeedUseCase()
        self.loadImageUseCase = NativeLoadImageUseCase()
        self.playbackPositionPublisher = self.player.playbackTime
        self.playbackDurationPublisher = self.player.playbackDuration
        
        observePlaybackState()
    }
    
    private func observePlaybackState() {
        self.player.playbackState
            .sink(receiveValue: { state in
                self.storeEpisodeOnPausedOrStopped(state)
            })
            .store(in: &disposables)
    }
    
    private func storeEpisodeOnPausedOrStopped(_ state: PlaybackState) {
        if state == PlaybackState.paused || state == PlaybackState.stopped {
            guard let episode = self.currentEpisode?
                    .copy(playbackPosition: PlaybackPosition(
                        position: self.playbackPositionPublisher.value,
                        duration: self.playbackDurationPublisher.value
                    )) else { return }
            self.storeEpisode(episode: episode)
        }
    }
    
    func loadFeed(_ url: String) {
        self.getStoredFeedUseCase.run(
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
        self.storeAndGetFeedUseCase.run(url: url) { result in
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
        self.loadImageUseCase.run(
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
        self.feedTitle = feed.title
        self.episodes = feed.episodes
        self.player.prepare(media: feed.episodes.map{ episode in episode.toMediaData() })
    }

    func episodeClicked(episode: Episode, playState: Bool) {
        self.playPause(episode.id, playState)
        self.currentEpisode = episode
    }
    
    func playPauseCurrent(playState: Bool) {
        guard let episodeId = self.currentEpisode?.id else { return }
        playPause(episodeId, playState)
    }
    
    private func playPause(_ episodeId: String, _ playState: Bool) {
        self.playing = playState
        self.player.playPause(mediaId: episodeId, playState: playState)
    }
    
    func forwardCurrentItem() {
        self.player.fastForward()
    }
    
    func replayCurrentItem() {
        self.player.rewind()
    }
    
    func skipToPrevious() {
        self.player.skipToPrevious()
    }
    
    func skipToNext() {
        self.player.skipToNext()
    }
    
    func seekTo(positionMillis: Int) {
        self.player.seekTo(position: positionMillis)
    }
    
    func playbackSpeed(speed: Float) {
        self.player.speed(speed: speed)
    }
    
    private func playingState(mediaId: String) -> Bool {
        return false
    }
    
    private func storeEpisode(episode: Episode) {
        self.storeEpisodeUseCase.run(
            episode: episode,
            onSuccess: { storedEpisode in
                print("Stored: 💾 \(storedEpisode.title)")
                self.currentEpisode = storedEpisode
                if let index = self.episodes.firstIndex(where: {$0.id == storedEpisode.id}) {
                    self.episodes[index] = storedEpisode
                }
            },
            onError: { error in
                print("Error storing: ❌ \(error)")
            })
    }
}
