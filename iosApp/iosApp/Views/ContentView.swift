import SwiftUI
import shared
import AVFoundation

func greet() -> String {
    return Greeting().greeting()
}

struct ContentView: View {
    
    @EnvironmentObject var viewModel: CastawayViewModel
    @State private var currentTime: TimeInterval = 0
    @State private var duration: TimeInterval = 1
    
    var body: some View {
        List(self.viewModel.episodes, id: \.id) { episode in
            EpisodeRowView(
                episode: episode,
                playing: self.viewModel.currentEpisode?.id == episode.id,
                playbackTime: currentTime,
                playbackDuration: duration
            ) { playing in
                self.viewModel.episodeClicked(episode: episode, playState: playing)
            }.onReceive(self.viewModel.playbackTimePublisher) { time in
                self.currentTime = time
            }.onReceive(self.viewModel.playbackDurationPublisher) { duration in
                self.duration = duration.doubleValue
            }
        }.onAppear {
            self.viewModel.fetchFeed()
        }
    }
}

#if DEBUG
struct ContentView_Previews: PreviewProvider {
    static var previews: some View {
        ContentView()
    }
}
#endif
