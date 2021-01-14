import SwiftUI
import shared

func greet() -> String {
    return Greeting().greeting()
}

struct ContentView: View {
    
    @ObservedObject var viewModel = CastawayViewModel()
    
    var body: some View {
        List(viewModel.episodes, id: \.id) { episode in
            EpisodeRowView(
                episode: episode,
                playing: episodePlaying(currentEpisode: self.viewModel.currentEpisode, episode: episode)
            ) { playing in
                self.viewModel.episodeClicked(episode: episode, playState: playing)
            }
        }.onAppear {
            self.viewModel.fetchFeed()
        }
    }
}

private func episodePlaying(currentEpisode: Episode?, episode: Episode) -> Bool {
    var playing = false
    if (currentEpisode != nil) {
        playing = currentEpisode!.id == episode.id
    }
    
    return playing
}


#if DEBUG
struct ContentView_Previews: PreviewProvider {
    static var previews: some View {
        ContentView()
    }
}
#endif
