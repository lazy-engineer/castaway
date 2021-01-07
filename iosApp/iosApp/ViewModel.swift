import Foundation
import shared
import FeedKit

class CastawayViewModel: ObservableObject {
    
    private let getFeedUseCase: GetFeedUseCase
    
    var rssFeed: RSSFeed?
    
    
    init(getFeedUseCase: GetFeedUseCase) {
        self.getFeedUseCase = getFeedUseCase
    }
    
    func fetchFeed() -> String {
        getFeedUseCase.invoke(params: "https://feeds.feedburner.com/blogspot/androiddevelopersbackstage") { xml in
            if let data = xml.data(using: String.Encoding(rawValue: String.Encoding.utf8.rawValue).rawValue) {
                let parser = FeedParser(data: data)
                parser.parseAsync(queue: DispatchQueue.global(qos: .userInitiated)) { (result) in
                    switch result {
                    case .success(let feed):
                        
                        print(feed.rssFeed?.title)
                        print(feed.rssFeed?.link)
                        print(feed.rssFeed?.items?.count)
        
                    case .failure(let error):
                        print(error)
                    }
                    
                    DispatchQueue.main.async {
                        // ..and update the UI
                    }
                }
            }
        } onError: { _ in
        } completionHandler: { (unit: KotlinUnit?, _: Error?) in
            
        }
        
        return "fetched"
    }

}
