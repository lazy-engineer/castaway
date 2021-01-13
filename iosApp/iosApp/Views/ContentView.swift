import SwiftUI
import shared

func greet() -> String {
    return Greeting().greeting()
}

struct ContentView: View {
    
    @ObservedObject var viewModel = CastawayViewModel()
    
    var body: some View {
        List(viewModel.episodes, id: \.id) { episode in
            EpisodeRowView(episode: episode) { playing in
                self.viewModel.playPauseEpisode(
                    episodeUrl: episode.audioUrl,
                    playState: playing
                )
            }
        }.onAppear {
            self.viewModel.fetchFeed()
        }
    }
}

struct ContentView_Previews: PreviewProvider {
    static var previews: some View {
        ContentView()
    }
}
