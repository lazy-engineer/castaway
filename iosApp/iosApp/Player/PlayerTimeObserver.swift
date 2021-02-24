import AVKit
import Combine

class PlayerTimeObserver {
    let publisher = CurrentValueSubject<Int64, Never>(0)
    private weak var player: AVPlayer?
    private var timeObservation: Any?
    private var paused = false
    
    init(player: AVPlayer) {
        self.player = player
        addPeriodicTimeObserver(player)
    }
    
    private func addPeriodicTimeObserver(_ player: AVPlayer) {
        let timeScale = CMTimeScale(NSEC_PER_SEC)
        let time = CMTime(seconds: 0.5, preferredTimescale: timeScale)
        
        timeObservation = player.addPeriodicTimeObserver(forInterval: time, queue: .main) { [weak self] time in
            guard let self = self else { return }
            guard !self.paused else { return }
            
            self.publisher.send(Int64(time.seconds * 1000))
        }
    }
    
    deinit {
        if let player = player,
           let observer = timeObservation {
            player.removeTimeObserver(observer)
        }
    }
    
    func pause(_ pause: Bool) {
        paused = pause
    }
}
