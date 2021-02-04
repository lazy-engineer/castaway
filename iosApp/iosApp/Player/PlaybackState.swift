import Foundation

public enum PlaybackState : Int {

    case unknown = 0

    case readyToPlay = 1

    case playing = 2
    
    case paused = 3
    
    case stopped = 4

    case failed = 5
}
