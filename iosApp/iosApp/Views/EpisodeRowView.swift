import SwiftUI
import shared


struct EpisodeRowView: View {
   
    @State var episode: Episode
    let playing: Bool
    let playbbackTime: TimeInterval
    let onPlayPause: (Bool) -> Void
   
    var body: some View {
        VStack {
            HStack {
                Text(episode.title)
                    .frame(minWidth: 0, maxWidth: .infinity, alignment: .leading)
                Button(action: {
                    self.onPlayPause(!playing)
                }) {
                    Image(systemName: playing ? "pause" : "play")
                        .foregroundColor(.blue)
                        .padding()
                }
            }.frame(height: 60)
            
            if #available(iOS 14.0, *) {
                ProgressView(value: playing ? playbbackTime : 0, total: 1000)
            } else {
                // Fallback on earlier versions
            }
            
        }
       
    }
}

#if DEBUG
struct EpisodeRowView_Previews: PreviewProvider {
    static var previews: some View {
        Group {
            EpisodeRowView(episode: dummy_episode_1, playing: true, playbbackTime: 0) {_ in }
                .previewLayout(.fixed(width: 360.0, height: 60.0))
            EpisodeRowView(episode: dummy_episode_1, playing: false, playbbackTime: 0) {_ in }
                .preferredColorScheme(.dark)
                .previewLayout(.fixed(width: 360.0, height: 60.0))
            EpisodeRowView(episode: dummy_episode_2, playing: false, playbbackTime: 0.0) {_ in }
                .previewLayout(.fixed(width: 360.0, height: 60.0))
        }
    }
}

let dummy_episode_1 = Episode(id: "String", title: "ADB 148: [Constraint|Motion][Layout|Editor] Tooooooooo Long Title. Wow even longer", subTitle: "String?", description: "String?", audioUrl: "String", imageUrl: "String?", author: "String?", playbackPosition: PlaybackPosition(position: 0, duration: nil, percentage: nil), isPlaying: false, podcastUrl: "String")

let dummy_episode_2 = Episode(id: "String", title: "Episode 154: It's a Wrap!", subTitle: "String?", description: "String?", audioUrl: "String", imageUrl: "String?", author: "String?", playbackPosition: PlaybackPosition(position: 0, duration: nil, percentage: nil), isPlaying: false, podcastUrl: "String")
#endif
