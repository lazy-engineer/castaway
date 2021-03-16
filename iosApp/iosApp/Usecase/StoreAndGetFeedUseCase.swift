import Foundation
import shared
import FeedKit

/**
 * At the moment kotlinx.serialization doesn't support xml parsing
 * (https://github.com/Kotlin/kotlinx.serialization/issues/188), that's why
 * we fetch remotely the xml string from shared module and parse it on each platform,
 * store the feed object locally and return it from there as a single source of truth
 */
class StoreAndGetFeedUseCase {
    private let getFeed: NativeGetFeedUseCase
    private let saveFeed: NativeSaveFeedUseCase
    
    init(
        getFeedUseCase: NativeGetFeedUseCase,
        saveFeedUseCase: NativeSaveFeedUseCase
    ) {
        getFeed = getFeedUseCase
        saveFeed = saveFeedUseCase
    }
    
    func run(url: String, completion: @escaping (Swift.Result<FeedData, Error>) -> Void) {
        getFeed.subscribe(
            url: url,
            scope: getFeed.coroutineScope,
            onSuccess: { xml in
                self.parseXml(xml as String, completion: { rssFeed in
                    
                    switch rssFeed {
                    case .success(let feed):
                        let feeddata = feed.toFeedData(url: url)
                        
                        self.saveFeed.subscribe(
                            feedData: feeddata,
                            scope: self.saveFeed.coroutineScope,
                            onSuccess: { savedFeed in
                                completion(.success(savedFeed))
                            },
                            onError: { error in
                                completion(.failure(NSError()))
                            })
                        
                    case .failure(let error):
                        completion(.failure(error))
                    }
                    
                })
            }, onError: { error in
                completion(.failure(NSError()))
            })
    }
    
    private func parseXml(_ xml: String, completion: @escaping (Swift.Result<RSSFeed, Error>) -> Void) {
        if let data = xml.data(using: String.Encoding(rawValue: String.Encoding.utf8.rawValue)) {
            let parser = FeedParser(data: data)
            parser.parseAsync(queue: DispatchQueue.global(qos: .userInitiated)) { (result) in
                
                var feedResult: Swift.Result<RSSFeed, Error>
                
                switch result {
                case .success(let feed):
                    if let rssFeed = feed.rssFeed {
                        feedResult = .success(rssFeed)
                    } else {
                        feedResult = .failure(NSError())
                    }
                    
                case .failure(let error):
                    feedResult = .failure(error)
                }
                
                DispatchQueue.main.async {
                    completion(feedResult)
                }
            }
        }
    }
}
