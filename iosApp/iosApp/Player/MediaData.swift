import Foundation
import UIKit
import shared

struct MediaData {
    let mediaId: String
    let mediaUri: String
    let title: String
    let podcastTitle: String
    var image: UIImage?
    var playbackPosition: Int64
    var duration: Int64
}
