import SwiftUI
import AVKit
import shared


struct EpisodeRowView: View {
    
    private let episode: Episode
    private let localPlayer = AVQueuePlayer.init()
    
    init(episode: Episode) {
        self.episode = episode
    }
    
    @State var playerPaused = true

    var body: some View {
        HStack {
            Text(episode.title)
                .frame(minWidth: 0, maxWidth: .infinity, alignment: .leading)
            Button(action: {
                self.playerPaused.toggle()
                playPauseEpisode(
                    episodeUrl: episode.audioUrl,
                    player: localPlayer,
                    playState: playerPaused
                )
            }) {
                Image(systemName: playerPaused ? "play" : "pause")
                    .foregroundColor(.blue)
                    .padding()
            }
        }.frame(height: 60)
    }
}

fileprivate func playPauseEpisode(episodeUrl: String, player: AVQueuePlayer, playState: Bool) {
    let url : URL? = URL.init(string: episodeUrl)
    if url != nil {
        let playerItem = AVPlayerItem.init(url: url!)
        player.insert(playerItem, after: nil)
    }
    
    if playState {
        player.pause()
    } else {
        player.play()
    }
}

struct EpisodeRowView_Previews: PreviewProvider {
    static var previews: some View {
        Group {
            EpisodeRowView(episode: dummy_episode_1)
                .previewLayout(.fixed(width: 360.0, height: 60.0))
            EpisodeRowView(episode: dummy_episode_1)
                .preferredColorScheme(.dark)
                .previewLayout(.fixed(width: 360.0, height: 60.0))
            EpisodeRowView(episode: dummy_episode_2)
                .previewLayout(.fixed(width: 360.0, height: 60.0))
        }
    }
}

let dummy_episode_1 = Episode(id: "String", title: "ADB 148: [Constraint|Motion][Layout|Editor] Tooooooooo Long Title. Wow even longer", subTitle: "String?", description: "String?", audioUrl: "String", imageUrl: "String?", author: "String?", playbackPosition: PlaybackPosition(position: 0, duration: nil, percentage: nil), isPlaying: false, podcastUrl: "String")

let dummy_episode_2 = Episode(id: "String", title: "Episode 154: It's a Wrap!", subTitle: "String?", description: "String?", audioUrl: "String", imageUrl: "String?", author: "String?", playbackPosition: PlaybackPosition(position: 0, duration: nil, percentage: nil), isPlaying: false, podcastUrl: "String")
