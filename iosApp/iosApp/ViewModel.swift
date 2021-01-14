import Foundation
import shared
import FeedKit
import AVKit

class CastawayViewModel: ObservableObject {
    
    private let storeAndGetFeedUseCase: StoreAndGetFeedUseCase
    private let player = CastawayPlayer()
    
    @Published var episodes = [Episode]()
    @Published var currentEpisode: Episode?
    
    init() {
        self.storeAndGetFeedUseCase = StoreAndGetFeedUseCase()
    }
    
    func fetchFeed() {
        self.storeAndGetFeedUseCase.run(url: "https://feeds.feedburner.com/blogspot/androiddevelopersbackstage") { result in
            switch result {
            case .success(let feed):
                self.episodes = feed.episodes
                
            case .failure(let error):
                print(error)
            }
        }
    }
    
    func mediaItemClicked(clickedItemId: String) {
        
    }
    
    func episodeClicked(episode: Episode, playState: Bool) {
        if episode != currentEpisode {
            self.currentEpisode = episode
        } else {
            self.currentEpisode = nil
        }
       
        self.player.playPause(mediaId: episode.audioUrl, playState: playState)
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
}
