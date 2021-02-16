import Foundation
import AVKit
import Combine
import shared

class CastawayPlayer {
    
    private let player: AVPlayer
    private var playerItems = [String : (MediaData, AVPlayerItem)]()
    private var playlist = [String]()
    
    private var timeObserverToken: Any?
    private var rateObserver: Any?
    private var currentItemObserver: Any?
    private var statusObserver: Any?
    private var durationObserver: Any?
    private var likelyToKeepUpObserver: Any?
    private var bufferEmptyObserver: Any?
    
    let playbackTime = CurrentValueSubject<Int64, Never>(0)
    let playbackDuration = CurrentValueSubject<Int64, Never>(1)
    let playbackSpeed = PassthroughSubject<Float, Never>()
    let playbackState = CurrentValueSubject<PlaybackState, Never>(PlaybackState.unknown)
    let nowPlaying = CurrentValueSubject<String?, Never>(nil)
    
    init() {
        self.player = AVPlayer.init()
        addPeriodicTimeObserver()
        observePlayer()
    }
    
    private func addPeriodicTimeObserver() {
        let timeScale = CMTimeScale(NSEC_PER_SEC)
        let time = CMTime(seconds: 0.5, preferredTimescale: timeScale)
        
        timeObserverToken = player.addPeriodicTimeObserver(forInterval: time, queue: .main) { [weak self] time in
            guard let self = self else { return }
            self.playbackTime.send(Int64(time.seconds * 1000))
        }
    }
    
    private func observePlayer() {
        rateObserver = player.observe(\.rate, options: [.initial, .old, .new]) { [weak self] (item, change) in
            guard let self = self else { return }
            
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
    
    private func sendNowPlayingItemKey(_ currentItem: AVPlayerItem?) {
        if let unwrappedItem = currentItem {
            self.playerItems.forEach { key, itemTuple in
                sendKeyIfPlayerItemFound(itemToFound: unwrappedItem, itemTuple: itemTuple)
            }
        }
    }
    
    private func sendKeyIfPlayerItemFound(itemToFound: AVPlayerItem, itemTuple: (MediaData, AVPlayerItem)) {
        if itemToFound == itemTuple.1 {
            self.nowPlaying.send(itemTuple.0.mediaId)
        }
    }
    
    private func observePlayerItem() {
        // track status
        self.statusObserver = self.player.currentItem?.observe(\.status, options: [.initial, .old, .new]) { [weak self] (item, change) in
            guard let self = self else { return }
            print("statusObserver: \(change)")
        }
        
        self.durationObserver = self.player.currentItem?.observe(\.duration, options: [.initial, .old, .new]) { [weak self] (item, change) in
            guard let self = self else { return }
            
            if let newDuration = change.newValue {
                self.sendDurationMillis(newDuration)
                
                if let nowPlayingKey = self.nowPlaying.value {
                    self.updateMediaItemDuration(nowPlayingKey, newDuration)
                }
            }
        }
        
        self.likelyToKeepUpObserver = self.player.currentItem?.observe(\.isPlaybackLikelyToKeepUp, options: [.initial, .old, .new]) { [weak self] (item, change) in
            guard let self = self else { return }
            
            if let readyToPlay = change.newValue {
                if readyToPlay == true {
                    self.playbackState.send(PlaybackState.readyToPlay)
                }
            }
        }
        
        // track buffer empty
        self.bufferEmptyObserver = self.player.currentItem?.observe(\.isPlaybackBufferEmpty, options: [.initial, .old, .new]) { [weak self] (item, change) in
            guard let self = self else { return }
            print("bufferEmptyObserver: \(change)")
        }
    }
    
    private func sendDurationMillis(_ newDuration: CMTime) {
        let durationMillies = self.duration(time: newDuration)
        if durationMillies > 0 {
            self.playbackDuration.send(durationMillies)
        }
    }
    
    private func updateMediaItemDuration(_ itemKey: String, _ newDuration: CMTime) {
        if let item = self.playerItems[itemKey] {
            var updatedItem = item
            updatedItem.0.duration = self.duration(time: newDuration)
            self.playerItems.updateValue((updatedItem.0, updatedItem.1), forKey: itemKey)
        }
    }
    
    private func removePeriodicTimeObserver() {
        if let timeObserverToken = timeObserverToken {
            player.removeTimeObserver(timeObserverToken)
            self.timeObserverToken = nil
        }
    }
    
    func subscribe() {
        
    }
    
    func unsubscribe() {
        
    }
    
    func prepare(media: [MediaData]) {
        prepareMediaData(media)
    }
    
    func prepare(block: () -> [MediaData]) {
        prepareMediaData(block())
    }
    
    private func prepareMediaData(_ media: [MediaData]) {
        self.playlist = media.map { mediaData in
            mediaData.mediaId
        }
        self.playerItems = Dictionary(grouping: media, by: { mediaData in mediaData.mediaId })
            .mapValues { media in (media.first!, media.first!.toAVPlayerItem()) }
    }
    
    func playPause(
        mediaId: String,
        playState: Bool
    ) {
        let playerItem: AVPlayerItem? = playerItems[mediaId]?.1
        if playerItem != nil {
            self.player.replaceCurrentItem(with: playerItem)
        }
        
        if playState {
            self.player.play()
            self.playbackState.send(PlaybackState.playing)
        } else {
            self.player.pause()
            self.playbackState.send(PlaybackState.paused)
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
        return self.duration(time: asset.duration)
    }
}
