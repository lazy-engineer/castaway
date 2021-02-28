import Foundation
import AVKit
import Combine
import shared

class CastawayPlayer {
    
    private let player: AVPlayer
    private var playerItems = [String : (MediaData, AVPlayerItem)]()
    private var playlist = [String]()
    
    private var disposables = Set<AnyCancellable>()
    
    private var rateObserver: Any?
    private var currentItemObserver: Any?
    private var statusObserver: Any?
    private var likelyToKeepUpObserver: Any?
    private var bufferEmptyObserver: Any?
    
    let playbackTimeObserver: PlayerTimeObserver
    let playbackDurationObserver: PlayerDurationObserver
    let playbackState = CurrentValueSubject<PlaybackState, Never>(PlaybackState.unknown)
    let playbackSpeed = PassthroughSubject<Float, Never>()
    let nowPlaying = CurrentValueSubject<String?, Never>(nil)
    
    init() {
        player = AVPlayer.init()
        playbackTimeObserver = PlayerTimeObserver(player: player)
        playbackDurationObserver = PlayerDurationObserver(player: player)
        observePlayer()
    }
    
    
    private func observePlayer() {
        playbackDurationObserver.publisher
            .sink(receiveValue: { duration in
                if let nowPlayingKey = self.nowPlaying.value {
                    self.updateMediaItemDuration(nowPlayingKey, duration)
                }
            }).store(in: &disposables)
        
        playbackTimeObserver.publisher
            .sink(receiveValue: { position in
                if let nowPlayingKey = self.nowPlaying.value {
                    self.updateMediaItemPosition(nowPlayingKey, position)
                }
            }).store(in: &disposables)
        
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
        
        NotificationCenter.default.addObserver(self, selector: #selector(playerDidFinishPlaying), name: .AVPlayerItemDidPlayToEndTime, object: nil)
    }
    
    @objc func playerDidFinishPlaying(note: NSNotification) {
        print("Media Item Finished 🎧")
        finishCurrentItem()
    }
    
    private func finishCurrentItem() {
        if hasNextItem() {
            skipToNext()
        } else {
            player.pause()
            playbackState.send(PlaybackState.finished)
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
    
    private func updateMediaItemDuration(_ itemKey: String, _ newDuration: Int64) {
        if let item = playerItems[itemKey] {
            var updatedItem = item
            updatedItem.0.duration = newDuration
            playerItems.updateValue((updatedItem.0, updatedItem.1), forKey: itemKey)
        }
    }
    
    private func updateMediaItemPosition(_ itemKey: String, _ newPosition: Int64) {
        if let item = playerItems[itemKey] {
            var updatedItem = item
            updatedItem.0.playbackPosition = newPosition
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
        seekTo(position: playbackTimeObserver.publisher.value + 30000)
    }
    
    func rewind() {
        seekTo(position: playbackTimeObserver.publisher.value - 30000)
    }
    
    func skipToNext() {
        guard let index = currentIndex() else { return }
        guard hasNextItem() else { return }
        
        let nextMediaId = playlist[index + 1]
        
        play(mediaId: nextMediaId, startAt: playerItems[nextMediaId]?.0.playbackPosition ?? 0)
    }
    
    func skipToPrevious() {
        guard let index = currentIndex() else { return }
        guard index - 1 >= 0 else { return }
        
        let previousMediaId = playlist[index - 1]
        
        play(mediaId: previousMediaId, startAt: playerItems[previousMediaId]?.0.playbackPosition ?? 0)
    }
    
    private func currentIndex() -> Int? {
        guard let currentId = nowPlaying.value else { return nil}
        guard let index = playlist.firstIndex(of: currentId) else { return nil }
        
        return index
    }
    
    private func hasNextItem() -> Bool {
        guard let index = currentIndex() else { return false }
        
        return playlist.count > index + 1
    }
    
    func speed(speed: Float) {
        player.rate = speed
    }
    
    func shuffle(shuffle: Bool) {
        
    }
    
    func repeatMode(repeat: Int) {
        
    }
}
