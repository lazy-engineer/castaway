import Foundation
import shared
import FeedKit
import AVFoundation

extension Episode {
    func copy(position: Int64) -> Episode {
        return self.doCopy(
            id: self.id,
            title: self.title,
            subTitle: self.subTitle,
            description: self.description_,
            audioUrl: self.audioUrl,
            imageUrl: self.imageUrl,
            author: self.author,
            playbackPosition: PlaybackPosition(
                position: position,
                duration: self.playbackPosition.duration
            ),
            isPlaying: self.isPlaying,
            podcastUrl: self.podcastUrl)
    }
}

extension Episode {
    func copy(duration: Int64) -> Episode {
        return self.doCopy(
            id: self.id,
            title: self.title,
            subTitle: self.subTitle,
            description: self.description_,
            audioUrl: self.audioUrl,
            imageUrl: self.imageUrl,
            author: self.author,
            playbackPosition: PlaybackPosition(
                position: self.playbackPosition.position,
                duration: duration
            ),
            isPlaying: self.isPlaying,
            podcastUrl: self.podcastUrl)
    }
}

extension Episode {
    func copy(playbackPosition: PlaybackPosition) -> Episode {
        return self.doCopy(
            id: self.id,
            title: self.title,
            subTitle: self.subTitle,
            description: self.description_,
            audioUrl: self.audioUrl,
            imageUrl: self.imageUrl,
            author: self.author,
            playbackPosition: playbackPosition,
            isPlaying: self.isPlaying,
            podcastUrl: self.podcastUrl)
    }
}

extension Episode {
    func toMediaData() -> MediaData {
        return MediaData.init(
            mediaId: self.id,
            mediaUri: self.audioUrl,
            playbackPosition: self.playbackPosition.position,
            duration: playbackPosition.duration
        )
    }
}

extension MediaData {
    func toAVPlayerItem() -> AVPlayerItem {
        return AVPlayerItem.init(url: URL.init(string: self.mediaUri)!)
    }
}

extension RSSFeed {
    func toFeedData(url: String) -> FeedData {
        let feedImage = self.feedImage()
        
        return FeedData(
            url: url,
            title: self.title!,
            image: feedImage,
            episodes: self.items!.compactMap({ $0.toEpisode(url: url, image: feedImage) }))
    }
    
    private func feedImage() -> String? {
        var feedImage: String? = nil
        
        if let image = self.image?.url {
            feedImage = image
        } else if let iTunesFeedImage = self.iTunes?.iTunesImage?.attributes?.href {
            feedImage = iTunesFeedImage
        }

        return feedImage
    }
}

extension RSSFeedItem {
    func toEpisode(url: String, image: String?) -> Episode? {
        guard let audioUrl = audioUrl() else { return nil }
        let episodeImage = self.episodeImage(feedImage: image)
        
        return Episode(
            id: UUID.init().uuidString,
            title: self.title!,
            subTitle: self.iTunes?.iTunesSubtitle,
            description: self.description,
            audioUrl: audioUrl,
            imageUrl: episodeImage,
            author: self.author,
            playbackPosition: PlaybackPosition(position: 0, duration: 1),
            isPlaying: false,
            podcastUrl: url)
    }
    
    private func audioUrl() -> String? {
        var audioUrl: String? = nil
        
        if let enclosureUrl = self.enclosure?.attributes?.url {
            audioUrl = enclosureUrl
        } else if let mediaUrl = self.media?.mediaContents?.first?.attributes?.url {
            audioUrl = mediaUrl
        }

        return audioUrl
    }
    
    private func episodeImage(feedImage: String?) -> String? {
        var episodeImage: String? = nil
        
        if let iTunesImage = self.iTunes?.iTunesImage?.attributes?.href {
            episodeImage = iTunesImage
        } else {
            episodeImage = feedImage
        }

        return episodeImage
    }
}

extension Dictionary where Value: Equatable {
    func allKeys(forValue val: Value) -> [Key] {
        return self.filter { $1 == val }.map { $0.0 }
    }
}
