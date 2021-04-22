import SwiftUI
import shared


struct EpisodeRowView: View {
    
    @EnvironmentObject var theme: ThemeNeumorphism
    
    @State var episode: Episode
    let playing: Bool
    let playbackTime: TimeInterval
    let playbackDuration: TimeInterval
    let onEpisodeClicked: () -> Void
    let onPlayPause: (Bool) -> Void
    
    @State var playingState = false
    
    var body: some View {
        HStack {
            VStack {
                Text(episode.title)
                    .font(.headline).bold().foregroundColor(theme.colorPalette.textColor)
                    .frame(minWidth: 0, maxWidth: .infinity, alignment: .leading)
                    .padding(.top, 16)
                
                PlaybackProgressView(playbackProgress: CGFloat(playbackTime / playbackDuration))
            }
            .padding(.trailing, 16)
            
            Toggle(isOn: $playingState) {
                Image(systemName: playing ? "pause.fill" : "play.fill")
                    .foregroundColor(playing ? .white : theme.colorPalette.primary)
            }
            .onChange(of: playingState, perform: { value in
                onPlayPause(playingState)
            })
            .onChange(of: playing, perform: { value in
                playingState = !playing
            })
            .toggleStyle(theme.style.roundToggleButtonStyle)
        }
        .contentShape(Rectangle())
        .frame(height: 70)
        .padding(.leading, 16)
        .padding(.trailing, 16)
        .padding(.bottom, 8)
        .padding(.top, 8)
        .onTapGesture(perform: {
            onEpisodeClicked()
        })
    }
}

#if DEBUG
struct EpisodeRowView_Previews: PreviewProvider {
    static var previews: some View {
        Group {
            EpisodeRowView(episode: dummy_episode_1, playing: true, playbackTime: 0, playbackDuration: 1, onEpisodeClicked: {}) {_ in }
                .previewLayout(.fixed(width: 360.0, height: 60.0))
            EpisodeRowView(episode: dummy_episode_1, playing: false, playbackTime: 0, playbackDuration: 1, onEpisodeClicked: {}) {_ in }
                .preferredColorScheme(.dark)
                .previewLayout(.fixed(width: 360.0, height: 60.0))
            EpisodeRowView(episode: dummy_episode_2, playing: false, playbackTime: 0.0, playbackDuration: 1, onEpisodeClicked: {}) {_ in }
                .previewLayout(.fixed(width: 360.0, height: 60.0))
        }
    }
}

let dummy_episode_1 = Episode(id: "String", title: "ADB 148: [Constraint|Motion][Layout|Editor] Tooooooooo Long Title. Wow even longer", subTitle: "String?", description: "String?", audioUrl: "String", imageUrl: "String?", author: "String?", playbackPosition: PlaybackPosition(position: 0, duration: 1), episode: 1, podcastUrl: "String")

let dummy_episode_2 = Episode(id: "String", title: "Episode 154: It's a Wrap!", subTitle: "String?", description: "String?", audioUrl: "String", imageUrl: "String?", author: "String?", playbackPosition: PlaybackPosition(position: 0, duration: 1), episode: 2, podcastUrl: "String")
#endif
