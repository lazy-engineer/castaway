import SwiftUI

struct NowPlayingScreen: View {
    
    @EnvironmentObject var viewModel: CastawayViewModel
    @State private var playbackPosition: TimeInterval = 0
    @State private var duration: TimeInterval = 1
    @State private var playing: Bool = true
    
    var body: some View {
        VStack {
            if let imageUrl = self.viewModel.feedImage {
                Image(uiImage: imageUrl)
                    .resizable()
                    .scaledToFill()
                    .frame(width: 200, height: 300)
                        .padding(86)
            } else {
                Image(systemName: "mic")
                    .resizable()
                    .scaledToFill()
                    .frame(width: 200, height: 300)
                    .padding(86)
                    .foregroundColor(.gray)
                    .onAppear {
                        guard let imageUrl = self.viewModel.currentEpisode?.imageUrl else { return }
                        self.viewModel.loadImage(imageUrl)
                    }
            }
            
            HStack(spacing: 30) {
                Spacer()
                Image(systemName: "backward.end.alt")
                    .resizable()
                    .scaledToFill()
                    .frame(width: 16, height: 16)
                    .foregroundColor(.blue)
                
                Image(systemName: "gobackward.30")
                    .resizable()
                    .scaledToFill()
                    .frame(width: 32, height: 32)
                    .foregroundColor(.blue)
                
                Button(action: {
                    self.viewModel.playPause(playState: !playing)
                    self.playing = !playing
                }) {
                    Image(systemName: playing ? "pause.circle.fill" : "play.circle.fill")
                        .resizable()
                        .scaledToFill()
                        .frame(width:52, height: 52)
                        .foregroundColor(.blue)
                }
                
                Image(systemName: "goforward.30")
                    .resizable()
                    .scaledToFill()
                    .frame(width: 32, height: 32)
                    .foregroundColor(.blue)
                
                Image(systemName: "forward.end.alt")
                    .resizable()
                    .scaledToFill()
                    .frame(width: 16, height: 16)
                    .foregroundColor(.blue)
                Spacer()
            }
            
            HStack {
                Text("\(Utility.formatSecondsToHMS(playbackPosition/1000))")
                    .padding(.leading, 8)
                Spacer()
                Text("\(Utility.formatSecondsToHMS(duration/1000))")
                    .padding(.trailing, 8)
            }.padding(.top, 48)
            
            Slider(value: $playbackPosition, in: 0...self.duration, step: 1)
                .padding(.leading, 8)
                .padding(.trailing, 8)
                .padding(.bottom, 48)
                .onReceive(self.viewModel.playbackPositionPublisher) { time in
                    self.playbackPosition = TimeInterval(time)
                }.onReceive(self.viewModel.playbackDurationPublisher) { duration in
                    self.duration = TimeInterval(duration)
                }
        }
    }
}

#if DEBUG
struct NowPlayingScreen_Previews: PreviewProvider {
    static var previews: some View {
        NowPlayingScreen()
    }
}
#endif
