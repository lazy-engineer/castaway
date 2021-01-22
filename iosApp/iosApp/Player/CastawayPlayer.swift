import Foundation
import AVKit
import Combine

class CastawayPlayer {
    
    private let player: AVPlayer
    private var timeObserverToken: Any?
    let playbackTimePublisher = PassthroughSubject<TimeInterval, Never>()
        
    init() {
        self.player = AVPlayer.init()
        addPeriodicTimeObserver()
    }
    
    func addPeriodicTimeObserver() {
        let timeScale = CMTimeScale(NSEC_PER_SEC)
        let time = CMTime(seconds: 0.1, preferredTimescale: timeScale)

        timeObserverToken = player.addPeriodicTimeObserver(forInterval: time, queue: .main) { [weak self] time in
            guard let self = self else { return }
            self.playbackTimePublisher.send(time.seconds)
        }
    }

    func removePeriodicTimeObserver() {
        if let timeObserverToken = timeObserverToken {
            player.removeTimeObserver(timeObserverToken)
            self.timeObserverToken = nil
            player.currentItem?.duration
        }
    }
    
    func subscribe() {
        
    }
    
    func unsubscribe() {
        
    }
    
    func prepare(playlist: [AVPlayerItem]) {
        
        
    }
    
    func prepare(block: () -> [AVPlayerItem]) {
        
    }
    
    func playPause(
        mediaId: String,
        playState: Bool
    ) {
        let url : URL? = URL.init(string: mediaId)
        if url != nil {
            let playerItem = AVPlayerItem.init(url: url!)
            self.player.replaceCurrentItem(with: playerItem)
        }
        
        if playState {
            self.player.play()
        } else {
            self.player.pause()
        }
    }
    
    func seekTo(position: Int) {
        
    }
    
    func fastForward() {
        
    }
    
    func rewind() {
        
    }
    
    func skipToNext() {
        
    }
    
    func skipToPrevious() {
        
    }
    
    func speed(speed: Float) {
        
    }
    
    func shuffle(shuffle: Bool) {
        
    }
    
    func repeatMode(repeat: Int) {
        
    }
    
    func duration(url: String) -> Float64? {
        guard let url = URL.init(string: url) else { return nil }
        
        let asset = AVURLAsset(url: url)
        let audioDuration = asset.duration
        return CMTimeGetSeconds(audioDuration)
    }
}
