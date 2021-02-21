import SwiftUI

struct EpisodeListView : View {
    
    @EnvironmentObject var viewModel: CastawayViewModel
    @State private var currentTime: TimeInterval = 0
    @State private var duration: TimeInterval = 1
    @State private var presentNowPlaying = false
    
    
    var body: some View {
        
        List(viewModel.episodes, id: \.id) { episode in
            
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
            .onReceive(viewModel.playbackPositionPublisher) { time in
                currentTime = TimeInterval(time)
            }
            .onReceive(viewModel.playbackDurationPublisher) { playbackDuration in
                duration = TimeInterval(playbackDuration)
            }
        }
        .onAppear {
            viewModel.loadFeed("https://atp.fm/rss")
        }
        .fullScreenCover(isPresented: $presentNowPlaying, onDismiss: {}) {
            NowPlayingScreen().environmentObject(viewModel)
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
