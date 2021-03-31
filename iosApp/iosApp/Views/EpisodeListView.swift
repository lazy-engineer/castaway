import SwiftUI

struct EpisodeListView : View {
    
    @EnvironmentObject var theme: ThemeNeumorphism
    @EnvironmentObject var viewModel: CastawayViewModel
    @State private var currentTime: TimeInterval = 0
    @State private var duration: TimeInterval = 1
    @State private var presentNowPlaying = false
    
    var body: some View {
        LazyVStack {
            ForEach(viewModel.episodes, id: \.id) { episode in
                let isCurrentEpisode: Bool = viewModel.currentEpisode?.id == episode.id
                let playing = (viewModel.playing) ? isCurrentEpisode : false
                let playbackTime = (isCurrentEpisode) ? currentTime : TimeInterval(episode.playbackPosition.position)
                let playbackDuration: TimeInterval = (isCurrentEpisode) ? duration : TimeInterval(episode.playbackPosition.duration)
                
                EpisodeRowView(
                    episode: episode,
                    playing: playing,
                    playbackTime: playbackTime,
                    playbackDuration: playbackDuration,
                    onEpisodeClicked: {
                        viewModel.episodeClicked(episode: episode, playState: true)
                        presentNowPlaying.toggle()
                    }
                ) { playing in
                    viewModel.episodeClicked(episode: episode, playState: playing)
                }
                .onReceive(viewModel.playbackPosition.publisher) { time in
                    guard duration > 1 else { return }
                    currentTime = TimeInterval(time)
                }
                .onReceive(viewModel.playbackDuration.publisher) { playbackDuration in
                    duration = TimeInterval(playbackDuration)
                }
                .environmentObject(theme)
            }
        }
        .padding(.top, 16)
        .background(theme.colorPalette.background)
        .fullScreenCover(isPresented: $presentNowPlaying, onDismiss: {}) {
            NowPlayingScreen()
                .environmentObject(viewModel)
                .environmentObject(theme)
        }
    }
}

#if DEBUG
struct EpisodeListView_Previews: PreviewProvider {
    static var previews: some View {
        EpisodeListView()
    }
}
#endif
