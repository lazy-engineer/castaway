import Foundation
import AVKit
import Combine
import shared

class CastawayPlayer {
    
    private let player: AVPlayer
    private var playerItems = [String : AVPlayerItem]()
    private var playlist = [String]()
    
    private var timeObserverToken: Any?
    private var rateObserver: Any?
    private var currentItemObserver: Any?
    private var statusObserver: Any?
    private var durationObserver: Any?
    private var likelyToKeepUpObserver: Any?
    private var bufferEmptyObserver: Any?
    
    let playbackTime = PassthroughSubject<TimeInterval, Never>()
    let playbackSpeed = PassthroughSubject<Float, Never>()
    let playbackDuration = PassthroughSubject<KotlinLong, Never>()
    let nowPlaying = PassthroughSubject<String, Never>()
    
    init() {
        self.player = AVPlayer.init()
        addPeriodicTimeObserver()
        observePlayer()
    }
    
    fileprivate func addPeriodicTimeObserver() {
        let timeScale = CMTimeScale(NSEC_PER_SEC)
        let time = CMTime(seconds: 0.5, preferredTimescale: timeScale)
        
        timeObserverToken = player.addPeriodicTimeObserver(forInterval: time, queue: .main) { [weak self] time in
            guard let self = self else { return }
            self.playbackTime.send(time.seconds * 1000)
        }
    }
    
    fileprivate func observePlayer() {
        rateObserver = player.observe(\.rate, options: [.initial, .old, .new]) { [weak self] (item, change) in
            guard let self = self else { return }
            print("rateObserver: \(change)")
            
            if let rate = change.newValue {
                self.playbackSpeed.send(rate)
            }
        }
        
        currentItemObserver = player.observe(\.currentItem, options: [.initial, .old, .new]) { [weak self] (item, change) in
            guard let self = self else { return }
            
            if let currentItem = change.newValue {
                self.observePlayerItem()
                self.sendNowPlayingItemKey(currentItem)
            }
        }
    }
    
    fileprivate func sendNowPlayingItemKey(_ currentItem: AVPlayerItem?) {
        if let unwrappedItem = currentItem {
            if let playerItemKey = self.playerItems.allKeys(forValue: unwrappedItem).first {
                self.nowPlaying.send(playerItemKey)
            }
        }
    }
    
    fileprivate func observePlayerItem() {
        // track status
        self.statusObserver = self.player.currentItem?.observe(\.status, options: [.initial, .old, .new]) { [weak self] (item, change) in
            guard let self = self else { return }
            print("statusObserver: \(change)")
        }
        
        self.durationObserver = self.player.currentItem?.observe(\.duration, options: [.initial, .old, .new]) { [weak self] (item, change) in
            guard let self = self else { return }
            
            if let newDuration = change.newValue {
                self.sendDurationMillis(newDuration)
            }
        }
        
        // track "likely to keep up"
        self.likelyToKeepUpObserver = self.player.currentItem?.observe(\.isPlaybackLikelyToKeepUp, options: [.initial, .old, .new]) { [weak self] (item, change) in
            guard let self = self else { return }
            print("likelyToKeepUpObserver: \(change)")
            
        }
        
        // track buffer empty
        self.bufferEmptyObserver = self.player.currentItem?.observe(\.isPlaybackBufferEmpty, options: [.initial, .old, .new]) { [weak self] (item, change) in
            guard let self = self else { return }
            print("bufferEmptyObserver: \(change)")
        }
    }
    
    fileprivate func sendDurationMillis(_ newDuration: CMTime) {
        let durationMillies = self.duration(time: newDuration)
        if durationMillies.intValue > 0 {
            self.playbackDuration.send(durationMillies)
        }
    }
    
    fileprivate func removePeriodicTimeObserver() {
        if let timeObserverToken = timeObserverToken {
            player.removeTimeObserver(timeObserverToken)
            self.timeObserverToken = nil
        }
    }
    
    func subscribe() {
        
    }
    
    func unsubscribe() {
        
    }
    
    func prepare(episodes: [Episode]) {
        self.playlist = episodes.map { episode in
            episode.id
        }
        self.playerItems = Dictionary(grouping: episodes, by: { episode in episode.id }).mapValues { episodes in
            episodes.first!.toAVPlayerItem()
        }
    }
    
    func prepare(block: () -> [String : AVPlayerItem]) {
        self.playerItems = block()
    }
    
    func playPause(
        mediaId: String,
        playState: Bool
    ) {
        let playerItem: AVPlayerItem? = playerItems[mediaId]
        if playerItem != nil {
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
        self.player.rate = speed
    }
    
    func shuffle(shuffle: Bool) {
        
    }
    
    func repeatMode(repeat: Int) {
        
    }
    
    func duration(time: CMTime) -> KotlinLong {
        let timemillis = CMTimeConvertScale(
            time,
            timescale:1000,
            method: CMTimeRoundingMethod.roundHalfAwayFromZero)
        
        return KotlinLong(value: timemillis.value)
    }
    
    func durationFromUrl(url: String) -> KotlinLong? {
        guard let url = URL.init(string: url) else { return nil }
        let asset = AVURLAsset(url: url)
        return self.duration(time: asset.duration)
    }
}
