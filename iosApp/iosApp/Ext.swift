import Foundation
import shared
import FeedKit
import AVFoundation

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

extension Episode {
    func copy(duration: KotlinLong) -> Episode {
        return self.doCopy(
            id: self.id,
            title: self.title,
            subTitle: self.subTitle,
            description: self.description,
            audioUrl: self.audioUrl,
            imageUrl: self.imageUrl,
            author: self.author,
            playbackPosition: PlaybackPosition(
                position: self.playbackPosition.position,
                duration: duration,
                percentage: self.playbackPosition.percentage
            ),
            isPlaying: self.isPlaying,
            podcastUrl: self.podcastUrl)
    }
}

extension Episode {
    func toAVPlayerItem() -> AVPlayerItem {
        return AVPlayerItem.init(url: URL.init(string: self.audioUrl)!)
    }
}

extension RSSFeed {
    func toFeedData(url: String) -> FeedData {
        return FeedData(
            url: url,
            title: self.title!,
            episodes: self.items!.map({ $0.toEpisode(url: url) }))
    }
}

extension RSSFeedItem {
    func toEpisode(url: String) -> Episode {
        return Episode(
            id: UUID.init().uuidString,
            title: self.title!,
            subTitle: self.iTunes?.iTunesSubtitle,
            description: self.description,
            audioUrl: (self.media?.mediaContents!.first!.attributes!.url)!,
            imageUrl: nil,
            author: self.author,
            playbackPosition: PlaybackPosition(position: 0, duration: nil, percentage: nil),
            isPlaying: false,
            podcastUrl: url)
    }
}
