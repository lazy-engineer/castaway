import Foundation
import shared
import FeedKit
import AVKit
import Combine

class CastawayViewModel: ObservableObject {
    
    private let storeAndGetFeedUseCase: StoreAndGetFeedUseCase
    private let storeEpisodeUseCase: NativeSaveEpisodeUseCase
    private let player = CastawayPlayer()
    
    @Published var episodes = [Episode]()
    @Published var currentEpisode: Episode?
    var playbackTimePublisher: PassthroughSubject<TimeInterval, Never>
    var playbackDurationPublisher: PassthroughSubject<KotlinLong, Never>
    
    init() {
        self.storeAndGetFeedUseCase = StoreAndGetFeedUseCase()
        self.storeEpisodeUseCase = NativeSaveEpisodeUseCase()
        self.playbackTimePublisher = self.player.playbackTime
        self.playbackDurationPublisher = self.player.playbackDuration
    }
    
    func fetchFeed() {
        self.storeAndGetFeedUseCase.run(url: "https://feeds.feedburner.com/blogspot/androiddevelopersbackstage") { result in
            switch result {
            case .success(let feed):
                self.episodes = feed.episodes
                self.player.prepare(media: feed.episodes.map{ episode in episode.toMediaData() })
            case .failure(let error):
                print(error)
            }
        }
    }
    
    func mediaItemClicked(clickedItemId: String) {
        
    }
    
    func episodeClicked(episode: Episode, playState: Bool) {
        self.player.playPause(mediaId: episode.id, playState: playState)
        
        if episode != currentEpisode {
            self.currentEpisode = episode
        } else {
            self.currentEpisode = nil
        }
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
                
            },
            onError: { error in
                
            })
    }
}
