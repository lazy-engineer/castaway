import Foundation
import AVKit

class CastawayPlayer {
    
    private let player: AVPlayer
    
    init() {
        self.player = AVPlayer.init()
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
}
