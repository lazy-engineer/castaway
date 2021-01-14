import SwiftUI
import shared

func greet() -> String {
    return Greeting().greeting()
}

struct ContentView: View {
    
    @ObservedObject var viewModel = CastawayViewModel()
    
    var body: some View {
        List(self.viewModel.episodes, id: \.id) { episode in
            EpisodeRowView(
                episode: episode,
                playing: self.viewModel.currentEpisode?.id == episode.id
            ) { playing in
                self.viewModel.episodeClicked(episode: episode, playState: playing)
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
