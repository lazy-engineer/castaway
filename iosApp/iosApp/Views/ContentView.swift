import SwiftUI
import shared
import AVFoundation

func greet() -> String {
    return Greeting().greeting()
}

struct ContentView: View {
    
    @ObservedObject var viewModel = CastawayViewModel()
    @State private var currentTime: TimeInterval = 0
    
    var body: some View {
        List(self.viewModel.episodes, id: \.id) { episode in
            EpisodeRowView(
                episode: episode,
                playing: self.viewModel.currentEpisode?.id == episode.id,
                playbbackTime: currentTime
            ) { playing in
                self.viewModel.episodeClicked(episode: episode, playState: playing)
            }.onReceive(self.viewModel.playbackTimePublisher) { time in
                self.currentTime = time
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
