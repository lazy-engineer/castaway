import SwiftUI
import AVKit
import shared


struct EpisodeRowView: View {
    
    let onPlayPause: (Bool) -> Void
    
    private let episode: Episode
    
    init(episode: Episode, onPlayPause: @escaping (Bool) -> Void) {
        self.episode = episode
        self.onPlayPause = onPlayPause
    }
    
    @State var isPlaying = false

    var body: some View {
        HStack {
            Text(episode.title)
                .frame(minWidth: 0, maxWidth: .infinity, alignment: .leading)
            Button(action: {
                self.isPlaying.toggle()
                self.onPlayPause(isPlaying)
            }) {
                Image(systemName: isPlaying ? "pause" : "play")
                    .foregroundColor(.blue)
                    .padding()
            }
        }.frame(height: 60)
    }
}

struct EpisodeRowView_Previews: PreviewProvider {
    static var previews: some View {
        Group {
            EpisodeRowView(episode: dummy_episode_1) {_ in }
                .previewLayout(.fixed(width: 360.0, height: 60.0))
            EpisodeRowView(episode: dummy_episode_1) {_ in }
                .preferredColorScheme(.dark)
                .previewLayout(.fixed(width: 360.0, height: 60.0))
            EpisodeRowView(episode: dummy_episode_2) {_ in }
                .previewLayout(.fixed(width: 360.0, height: 60.0))
        }
    }
}

let dummy_episode_1 = Episode(id: "String", title: "ADB 148: [Constraint|Motion][Layout|Editor] Tooooooooo Long Title. Wow even longer", subTitle: "String?", description: "String?", audioUrl: "String", imageUrl: "String?", author: "String?", playbackPosition: PlaybackPosition(position: 0, duration: nil, percentage: nil), isPlaying: false, podcastUrl: "String")

let dummy_episode_2 = Episode(id: "String", title: "Episode 154: It's a Wrap!", subTitle: "String?", description: "String?", audioUrl: "String", imageUrl: "String?", author: "String?", playbackPosition: PlaybackPosition(position: 0, duration: nil, percentage: nil), isPlaying: false, podcastUrl: "String")
