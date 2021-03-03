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
                        .padding(24)
                }
            }
            
            if let imageUrl = viewModel.feedImage {
                Image(uiImage: imageUrl)
                    .resizable()
                    .scaledToFill()
                    .frame(width: 200, height: 250)
                    .padding(48)
            } else {
                Image(systemName: "mic")
                    .resizable()
                    .scaledToFill()
                    .frame(width: 200, height: 250)
                    .padding(48)
                    .foregroundColor(.gray)
            }
            
            Text(viewModel.currentEpisode?.title ?? "")
                .padding(16)
                .fixedSize(horizontal: false, vertical: true)
                .multilineTextAlignment(.center)
            
            HStack(spacing: 24) {
                Spacer()
                
                Button(action: {
                    viewModel.skipToPrevious()
                }) {
                    Image(systemName: "backward.end.alt")
                        .resizable()
                        .scaledToFill()
                        .frame(width: 16, height: 16)
                        .foregroundColor(.blue)
                }
                
                Button(action: {
                    viewModel.replayCurrentItem()
                }) {
                    Image(systemName: "gobackward.30")
                        .resizable()
                        .scaledToFill()
                        .frame(width: 32, height: 32)
                        .foregroundColor(.blue)
                }
                
                Button(action: {
                    viewModel.playPauseCurrent(playState: !viewModel.playing)
                }) {
                    Image(systemName: viewModel.playing ? "pause.circle.fill" : "play.circle.fill")
                        .resizable()
                        .scaledToFill()
                        .frame(width:64, height: 64)
                        .foregroundColor(.blue)
                }
                
                Button(action: {
                    viewModel.forwardCurrentItem()
                }) {
                    Image(systemName: "goforward.30")
                        .resizable()
                        .scaledToFill()
                        .frame(width: 32, height: 32)
                        .foregroundColor(.blue)
                }
                
                Button(action: {
                    viewModel.skipToNext()
                }) {
                    Image(systemName: "forward.end.alt")
                        .resizable()
                        .scaledToFill()
                        .frame(width: 16, height: 16)
                        .foregroundColor(.blue)
                }
                
                Spacer()
            }
            .padding(16)
            
            HStack {
                Text("\(Utility.formatSecondsToHMS(playbackPosition/1000))")
                    .font(.subheadline)
                    .padding(.leading, 8)
                Spacer()
                Text("\(Utility.formatSecondsToHMS(duration/1000))")
                    .font(.subheadline)
                    .padding(.trailing, 8)
            }
            .padding(.top, 36)
            
            Slider(value: $playbackPosition, in: 0...duration, step: 1, onEditingChanged: sliderEditingChanged)
                .padding(.leading, 8)
                .padding(.trailing, 8)
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
            
            HStack {
                Button(action: {
                    viewModel.changePlaybackSpeed()
                }) {
                    Text("\(String(format: "%.1f", viewModel.playbackSpeed))x")
                        .foregroundColor(.blue)
                }
                .padding(.leading, 24)
                .padding(.bottom, 24)
                .padding(.top, 8)
                
                Spacer()
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
