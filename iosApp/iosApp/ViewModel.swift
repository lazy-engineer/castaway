import Foundation
import shared
import FeedKit
import AVKit

class CastawayViewModel: ObservableObject {
    
    private let storeAndGetFeedUseCase: StoreAndGetFeedUseCase
    private let localPlayer = AVPlayer.init()
    
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
    
    func playPauseEpisode(episodeUrl: String, playState: Bool) {
        let url : URL? = URL.init(string: episodeUrl)
        if url != nil {
            let playerItem = AVPlayerItem.init(url: url!)
            localPlayer.replaceCurrentItem(with: playerItem)
        }
        
        if playState {
            localPlayer.play()
        } else {
            localPlayer.pause()
        }
    }
    
    func mediaItemClicked(clickedItemId: String) {
       
    }

    func episodeClicked(episode: Episode) {
       
    }

    func forwardCurrentItem() {
        
    }

    func replayCurrentItem() {
       
    }

    func skipToPrevious() {
       
    }

    func skipToNext() {

    }

    func seekTo(positionMillis: Int) {
      
    }

    func playbackSpeed(speed: Float) {
       
    }

    private func playingState(mediaId: String) -> Bool {
        return false
    }
}
