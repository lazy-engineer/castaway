import Foundation
import shared

extension Episode {
    func copy(playbackPosition: Int64) -> Episode {
        return self.doCopy(
            id: self.id,
            title: self.title,
            subTitle: self.subTitle,
            description: self.description,
            audioUrl: self.audioUrl,
            imageUrl: self.imageUrl,
            author: self.author,
            playbackPosition: PlaybackPosition(
                position: playbackPosition,
                duration: self.playbackPosition.duration,
                percentage: self.playbackPosition.percentage
            ),
            isPlaying: self.isPlaying,
            podcastUrl: self.podcastUrl)
    }
}
