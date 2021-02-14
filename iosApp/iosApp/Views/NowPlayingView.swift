import SwiftUI

struct NowPlayingView: View {
    
    @EnvironmentObject var viewModel: CastawayViewModel
    @State private var playbackPosition: TimeInterval = 0
    @State private var duration: TimeInterval = 1
    @State private var playing: Bool = true
    
    var body: some View {
        VStack {
            Image(systemName: "mic")
                .resizable()
                .scaledToFill()
                .frame(width: 200, height: 300)
                .foregroundColor(.gray)
                .padding(86)
            
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
            
            Slider(value: $playbackPosition, in: 0...self.duration, step: 0.1)
                .padding(.leading, 8)
                .padding(.trailing, 8)
                .padding(.bottom, 48)
                .onReceive(self.viewModel.playbackTimePublisher) { time in
                    self.playbackPosition = time
                }.onReceive(self.viewModel.playbackDurationPublisher) { duration in
                    self.duration = duration.doubleValue
                }
        }
    }
}

#if DEBUG
struct NowPlayingView_Previews: PreviewProvider {
    static var previews: some View {
        NowPlayingView()
    }
}
#endif
