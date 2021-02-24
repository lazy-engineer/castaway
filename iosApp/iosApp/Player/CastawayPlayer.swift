import Foundation
import AVKit
import Combine
import shared

class CastawayPlayer {
    
    private let player: AVPlayer
    private var playerItems = [String : (MediaData, AVPlayerItem)]()
    private var playlist = [String]()
    
    private var rateObserver: Any?
    private var currentItemObserver: Any?
    private var statusObserver: Any?
    private var durationObserver: Any?
    private var likelyToKeepUpObserver: Any?
    private var bufferEmptyObserver: Any?
    
    let playbackTimeObserver: PlayerTimeObserver
    let playbackDuration = CurrentValueSubject<Int64, Never>(1)
    let playbackSpeed = PassthroughSubject<Float, Never>()
    let playbackState = CurrentValueSubject<PlaybackState, Never>(PlaybackState.unknown)
    let nowPlaying = CurrentValueSubject<String?, Never>(nil)
    
    init() {
        player = AVPlayer.init()
        playbackTimeObserver = PlayerTimeObserver(player: player)
        observePlayer()
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
            playerItems.forEach { key, itemTuple in
                sendKeyIfPlayerItemFound(itemToFound: unwrappedItem, itemTuple: itemTuple)
            }
        }
    }
    
    private func sendKeyIfPlayerItemFound(itemToFound: AVPlayerItem, itemTuple: (MediaData, AVPlayerItem)) {
        if itemToFound == itemTuple.1 {
            nowPlaying.send(itemTuple.0.mediaId)
        }
    }
    
    private func observePlayerItem() {
        // track status
        statusObserver = player.currentItem?.observe(\.status, options: [.initial, .old, .new]) { [weak self] (item, change) in
            guard let self = self else { return }
            print("statusObserver: \(change)")
        }
        
        durationObserver = player.currentItem?.observe(\.duration, options: [.initial, .old, .new]) { [weak self] (item, change) in
            guard let self = self else { return }
            
            if let newDuration = change.newValue {
                self.sendDurationMillis(newDuration)
                
                if let nowPlayingKey = self.nowPlaying.value {
                    self.updateMediaItemDuration(nowPlayingKey, newDuration)
                }
            }
        }
        
        likelyToKeepUpObserver = player.currentItem?.observe(\.isPlaybackLikelyToKeepUp, options: [.initial, .old, .new]) { [weak self] (item, change) in
            guard let self = self else { return }
            
            if let readyToPlay = change.newValue {
                if readyToPlay == true {
                    self.playbackState.send(PlaybackState.readyToPlay)
                }
            }
        }
        
        // track buffer empty
        bufferEmptyObserver = player.currentItem?.observe(\.isPlaybackBufferEmpty, options: [.initial, .old, .new]) { [weak self] (item, change) in
            guard let self = self else { return }
            print("bufferEmptyObserver: \(change)")
        }
    }
    
    private func sendDurationMillis(_ newDuration: CMTime) {
        let durationMillies = duration(time: newDuration)
        if durationMillies > 0 {
            playbackDuration.send(durationMillies)
        }
    }
    
    private func updateMediaItemDuration(_ itemKey: String, _ newDuration: CMTime) {
        if let item = playerItems[itemKey] {
            var updatedItem = item
            updatedItem.0.duration = duration(time: newDuration)
            playerItems.updateValue((updatedItem.0, updatedItem.1), forKey: itemKey)
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
        playlist = media.map { mediaData in
            mediaData.mediaId
        }
        playerItems = Dictionary(grouping: media, by: { mediaData in mediaData.mediaId })
            .mapValues { media in (media.first!, media.first!.toAVPlayerItem()) }
    }
    
    func play(
        mediaId: String,
        startAt: Int64
    ) {
        preparePlayer(mediaId, startAt)
        
        player.play()
        playbackState.send(PlaybackState.playing)
    }
    
    func resume() {
        player.play()
        playbackState.send(PlaybackState.playing)
    }
    
    func pause() {
        player.pause()
        playbackState.send(PlaybackState.paused)
    }
    
    private func preparePlayer(_ mediaId: String, _ startAt: Int64) {
        guard let playerItem = playerItems[mediaId]?.1 else { return }
        if player.currentItem != playerItem {
            playbackState.send(PlaybackState.stopped)
            player.replaceCurrentItem(with: playerItem)
            seekTo(position: startAt)
        }
    }
    
    func seekTo(position: Int64) {
        playbackState.send(PlaybackState.buffering)
        player.seek(to: CMTimeMake(value:position, timescale: 1000)) { _ in
            self.playbackState.send(PlaybackState.playing)
        }
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
        player.rate = speed
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
        return duration(time: asset.duration)
    }
}
