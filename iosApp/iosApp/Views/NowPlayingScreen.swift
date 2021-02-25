import SwiftUI

struct NowPlayingScreen: View {
    
    @Environment(\.presentationMode) var presentationMode
    
    @EnvironmentObject var viewModel: CastawayViewModel
    @State private var playbackPosition: TimeInterval = 0
    @State private var duration: TimeInterval = 1
    
    var body: some View {
        VStack {
            HStack {
                Spacer()
                Button(action: {
                    presentationMode.wrappedValue.dismiss()
                }) {
                    Image(systemName: "chevron.down")
                        .foregroundColor(.blue)
                        .padding()
                }
            }
            
            if let imageUrl = viewModel.feedImage {
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
                        guard let imageUrl = viewModel.currentEpisode?.imageUrl else { return }
                        viewModel.loadImage(imageUrl)
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
                    viewModel.playPauseCurrent(playState: !viewModel.playing)
                }) {
                    Image(systemName: viewModel.playing ? "pause.circle.fill" : "play.circle.fill")
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
            
            Slider(value: $playbackPosition, in: 0...duration, step: 1, onEditingChanged: sliderEditingChanged)
                .padding(.leading, 8)
                .padding(.trailing, 8)
                .padding(.bottom, 48)
                .onReceive(viewModel.playbackDuration.publisher) { playbackDuration in
                    duration = TimeInterval(playbackDuration)
                }
                .onReceive(viewModel.playbackStatePublisher) { state in
                    guard state == PlaybackState.playing else { return }
                    viewModel.playbackPosition.pause(false)
                }
                .onReceive(viewModel.playbackPosition.publisher) { time in
                    guard duration > 1 else { return }
                    playbackPosition = TimeInterval(time)
                }
        }
    }
    
    private func sliderEditingChanged(editingStarted: Bool) {
        if editingStarted {
            viewModel.playbackPosition.pause(true)
        } else {
            viewModel.seekTo(positionMillis: Int64(playbackPosition))
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
