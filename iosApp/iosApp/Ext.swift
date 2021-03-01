import shared
import FeedKit
import AVFoundation

extension Episode {
    func copy(position: Int64) -> Episode {
        return doCopy(
            id: id,
            title: title,
            subTitle: subTitle,
            description: description_,
            audioUrl: audioUrl,
            imageUrl: imageUrl,
            image: image,
            author: author,
            playbackPosition: PlaybackPosition(
                position: position,
                duration: playbackPosition.duration
            ),
            isPlaying: isPlaying,
            episode: episode,
            podcastUrl: podcastUrl)
    }
}

extension Episode {
    func copy(duration: Int64) -> Episode {
        return doCopy(
            id: id,
            title: title,
            subTitle: subTitle,
            description: description_,
            audioUrl: audioUrl,
            imageUrl: imageUrl,
            image: image,
            author: author,
            playbackPosition: PlaybackPosition(
                position: playbackPosition.position,
                duration: duration
            ),
            isPlaying: isPlaying,
            episode: episode,
            podcastUrl: podcastUrl)
    }
}

extension Episode {
    func copy(playbackPosition: PlaybackPosition) -> Episode {
        return doCopy(
            id: id,
            title: title,
            subTitle: subTitle,
            description: description_,
            audioUrl: audioUrl,
            imageUrl: imageUrl,
            image: image,
            author: author,
            playbackPosition: playbackPosition,
            isPlaying: isPlaying,
            episode: episode,
            podcastUrl: podcastUrl)
    }
}

extension Episode {
    func toMediaData() -> MediaData {
        return MediaData.init(
            mediaId: id,
            mediaUri: audioUrl,
            title: title,
            podcastTitle: "Accidental Tech Podcast",
            image: image,
            playbackPosition: playbackPosition.position,
            duration: playbackPosition.duration
        )
    }
}

extension MediaData {
    func toAVPlayerItem() -> AVPlayerItem {
        return AVPlayerItem.init(url: URL.init(string: mediaUri)!)
    }
}

extension RSSFeed {
    func toFeedData(url: String) -> FeedData {
        let feedImage = self.feedImage()
        
        return FeedData(
            url: url,
            title: title!,
            image: feedImage,
            episodes: items!.enumerated().compactMap({ $0.element.toEpisode(url: url, image: feedImage, index: Int32($0.offset)) }))
    }
    
    private func feedImage() -> String? {
        var feedImage: String? = nil
        
        if let image = image?.url {
            feedImage = image
        } else if let iTunesFeedImage = iTunes?.iTunesImage?.attributes?.href {
            feedImage = iTunesFeedImage
        }

        return feedImage
    }
}

extension RSSFeedItem {
    func toEpisode(url: String, image: String?, index: Int32) -> Episode? {
        guard let audioUrl = audioUrl() else { return nil }
        let episodeImage = self.episodeImage(feedImage: image)
        
        return Episode(
            id: UUID.init().uuidString,
            title: title!,
            subTitle: iTunes?.iTunesSubtitle,
            description: description,
            audioUrl: audioUrl,
            imageUrl: episodeImage,
            image: nil,
            author: author,
            playbackPosition: PlaybackPosition(position: 0, duration: 1),
            isPlaying: false,
            episode: index,
            podcastUrl: url)
    }
    
    private func audioUrl() -> String? {
        var audioUrl: String? = nil
        
        if let enclosureUrl = enclosure?.attributes?.url {
            audioUrl = enclosureUrl
        } else if let mediaUrl = media?.mediaContents?.first?.attributes?.url {
            audioUrl = mediaUrl
        }

        return audioUrl
    }
    
    private func episodeImage(feedImage: String?) -> String? {
        var episodeImage: String? = feedImage
        
        if let iTunesImage = iTunes?.iTunesImage?.attributes?.href {
            episodeImage = iTunesImage
        }

        return episodeImage
    }
}

extension Dictionary where Value: Equatable {
    func allKeys(forValue val: Value) -> [Key] {
        return self.filter { $1 == val }.map { $0.0 }
    }
}
