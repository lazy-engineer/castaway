import Foundation
import AVKit
import Combine
import shared

class CastawayPlayer {
    
    private let player: AVPlayer
    private var timeObserverToken: Any?
    private var rateObserver: Any?
    private var statusObserver: Any?
    private var durationObserver: Any?
    private var likelyToKeepUpObserver: Any?
    private var bufferEmptyObserver: Any?
    
    let playbackTime = PassthroughSubject<TimeInterval, Never>()
    let nowPlaying = PassthroughSubject<AVPlayerItem, Never>()
    
    init() {
        self.player = AVPlayer.init()
        addPeriodicTimeObserver()
    }
    
    func addPeriodicTimeObserver() {
        let timeScale = CMTimeScale(NSEC_PER_SEC)
        let time = CMTime(seconds: 0.1, preferredTimescale: timeScale)
        
        timeObserverToken = player.addPeriodicTimeObserver(forInterval: time, queue: .main) { [weak self] time in
            guard let self = self else { return }
            self.playbackTime.send(time.seconds)
        }
    }
    
    func obeserver() {
        // track playback rate
        rateObserver = player.observe(\.rate, options: [.initial, .old, .new]) { [weak self] (item, change) in
            guard let self = self else { return }
        }
        
        // track status
        statusObserver = player.currentItem?.observe(\.status, options: [.initial, .old, .new]) { [weak self] (item, change) in
            guard let self = self else { return }
        }
        
        // track duration
        durationObserver = player.currentItem?.observe(\.duration, options: [.initial, .old, .new]) { [weak self] (item, change) in
            guard let self = self else { return }
        }
        
        // track "likely to keep up"
        likelyToKeepUpObserver = player.currentItem?.observe(\.isPlaybackLikelyToKeepUp, options: [.initial, .old, .new]) { [weak self] (item, change) in
            guard let self = self else { return }
            
        }
        
        // track buffer empty
        bufferEmptyObserver = player.currentItem?.observe(\.isPlaybackBufferEmpty, options: [.initial, .old, .new]) { [weak self] (item, change) in
            guard let self = self else { return }
        }
    }
    
    func removePeriodicTimeObserver() {
        if let timeObserverToken = timeObserverToken {
            player.removeTimeObserver(timeObserverToken)
            self.timeObserverToken = nil
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
    
    func duration(url: String) -> KotlinLong? {
        guard let url = URL.init(string: url) else { return nil }
        
        let asset = AVURLAsset(url: url)
        let timemillis = CMTimeConvertScale(
            asset.duration,
            timescale:1000,
            method: CMTimeRoundingMethod.roundHalfAwayFromZero)
        
        return KotlinLong(value: timemillis.value)
    }
}
