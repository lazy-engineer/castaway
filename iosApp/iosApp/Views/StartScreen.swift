import SwiftUI
import shared
import AVFoundation

func greet() -> String {
    return Greeting().greeting()
}

struct StartScreen: View {
    
    @EnvironmentObject var viewModel: CastawayViewModel
    @State private var currentTime: TimeInterval = 0
    @State private var duration: TimeInterval = 1
    @State private var presentNowPlaying = false
    
    var body: some View {
        NavigationView {
            List(self.viewModel.episodes, id: \.id) { episode in
                EpisodeRowView(
                    episode: episode,
                    playing: (self.viewModel.playing) ? self.viewModel.currentEpisode?.id == episode.id : false,
                    playbackTime: (self.viewModel.currentEpisode?.id == episode.id) ? currentTime : TimeInterval(episode.playbackPosition.position),
                    playbackDuration: (self.viewModel.currentEpisode?.id == episode.id) ? duration : TimeInterval(episode.playbackPosition.duration),
                    onEpisodeClicked: {
                        self.viewModel.episodeClicked(episode: episode, playState: true)
                        presentNowPlaying.toggle()
                    }
                ) { playing in
                    self.viewModel.episodeClicked(episode: episode, playState: playing)
                }.onReceive(self.viewModel.playbackPositionPublisher) { time in
                    self.currentTime = TimeInterval(time)
                }.onReceive(self.viewModel.playbackDurationPublisher) { duration in
                    self.duration = TimeInterval(duration)
                }
            }.onAppear {
                self.viewModel.loadFeed("https://atp.fm/rss")
            }
            .navigationBarTitle(self.viewModel.feedTitle)
        }
        .fullScreenCover(isPresented: $presentNowPlaying,  onDismiss: {}) {
            NowPlayingScreen().environmentObject(viewModel)
        }
    }
}

#if DEBUG
struct StartScreen_Previews: PreviewProvider {
    static var previews: some View {
        StartScreen()
    }
}
#endif
