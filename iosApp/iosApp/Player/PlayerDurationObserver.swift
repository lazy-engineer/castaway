import AVKit
import Combine

class PlayerDurationObserver {
    let publisher = CurrentValueSubject<Int64, Never>(1)
    private var cancellable: AnyCancellable?
    private var paused = false
    
    init(player: AVPlayer) {
        addDurationObserver(player)
    }
    
    private func addDurationObserver(_ player: AVPlayer) {        
        let durationKeyPath: KeyPath<AVPlayer, CMTime?> = \.currentItem?.duration
        cancellable = player.publisher(for: durationKeyPath).sink { duration in
            guard let duration = duration else { return }
            guard !self.paused else { return }
            self.sendDurationMillis(duration)
        }
    }
    
    private func sendDurationMillis(_ newDuration: CMTime) {
        guard newDuration.isNumeric else { return }
        if newDuration.seconds > 0 {
            publisher.send(duration(time: newDuration))
        }
    }
    
    func pause(_ pause: Bool) {
        paused = pause
    }
    
    deinit {
        cancellable?.cancel()
    }
    
    func duration(time: CMTime) -> Int64 {
        let timemillis = CMTimeConvertScale(
            time,
            timescale:1000,
            method: CMTimeRoundingMethod.roundHalfAwayFromZero)
        
        return timemillis.value
    }
    
    func durationFromUrl(url: String) -> Int64? {
        guard let url = URL.init(string: url) else { return nil }
        let asset = AVURLAsset(url: url)
        return duration(time: asset.duration)
    }
}
