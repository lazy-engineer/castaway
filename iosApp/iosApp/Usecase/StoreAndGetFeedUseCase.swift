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
    private let getFeedUseCase: NativeGetFeedUseCase
    private let saveFeedUseCase: NativeSaveFeedUseCase
    
    init() {
        self.getFeedUseCase = NativeGetFeedUseCase()
        self.saveFeedUseCase = NativeSaveFeedUseCase()
    }
    
    func run(url: String, completion: @escaping (Swift.Result<FeedData, Error>) -> Void) {
        self.getFeedUseCase.run(
            url: url,
            onSuccess: { xml in
                self.parseXml(xml, completion: { rssFeed in
                    
                    switch rssFeed {
                    case .success(let feed):
                        let feeddata = feed.toFeedData(url: url)
                        
                        self.saveFeedUseCase.run(
                            feedData: feeddata,
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
    
    fileprivate func parseXml(_ xml: String, completion: @escaping (Swift.Result<RSSFeed, Error>) -> Void) {
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

extension RSSFeed {
    func toFeedData(url: String) -> FeedData {
        return FeedData(url: url, title: self.title!, episodes: self.items!.map({ $0.toEpisode(url: url) }))
    }
}

extension RSSFeedItem {
    func toEpisode(url: String) -> Episode_ {
        return Episode_(id: UUID.init().uuidString, title: self.title!, subTitle: self.iTunes?.iTunesSubtitle, description: self.description, audioUrl: (self.media?.mediaContents!.first!.attributes!.url)!, imageUrl: nil, author: self.author, playbackPosition: PlaybackPosition(position: 0, duration: nil, percentage: nil), isPlaying: false, podcastUrl: url)
    }
}
