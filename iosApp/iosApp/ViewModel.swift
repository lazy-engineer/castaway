import Foundation
import shared
import FeedKit

class CastawayViewModel: ObservableObject {
    
    private let storeAndGetFeedUseCase: StoreAndGetFeedUseCase
    
    @Published var episodes = [Episode]()
    
    init() {
        self.storeAndGetFeedUseCase = StoreAndGetFeedUseCase()
    }
    
    func fetchFeed() {
        self.storeAndGetFeedUseCase.run(url: "https://feeds.feedburner.com/blogspot/androiddevelopersbackstage") { result in
            switch result {
            case .success(let feed):
                print(feed)
                self.episodes = feed.episodes
                
            case .failure(let error):
                print(error)
            }
        }
    }
}
