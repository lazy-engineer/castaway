import Foundation
import shared


class CastawayViewModel: ObservableObject {
    
    private let getFeedUseCase: GetFeedUseCase
    
    init(getFeedUseCase: GetFeedUseCase) {
        self.getFeedUseCase = getFeedUseCase
    }
    
    func fetchFeed() {
        getFeedUseCase.invoke(params: "https://feeds.feedburner.com/blogspot/androiddevelopersbackstage") { xml in
            print(xml)
        } onError: { _ in
        } completionHandler: {_,_ in }
    }

}
