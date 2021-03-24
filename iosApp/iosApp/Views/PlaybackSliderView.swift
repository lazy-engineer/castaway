import SwiftUI

struct PlaybackSliderView: View {
    
    @EnvironmentObject var theme: ThemeNeumorphismLight
    @EnvironmentObject var viewModel: CastawayViewModel
    @State private var playbackPosition: TimeInterval = 0
    @State private var duration: TimeInterval = 1
    
    var body: some View {
        VStack {
            HStack {
                Text("\(Utility.formatSecondsToHMS(playbackPosition/1000))")
                    .font(.subheadline).bold().foregroundColor(theme.colorPalette.textColor)
                    .padding(.leading, 8)
                Spacer()
                Text("\(Utility.formatSecondsToHMS(duration/1000))")
                    .font(.subheadline).bold().foregroundColor(theme.colorPalette.textColor)
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

struct PlaybackSliderView_Previews: PreviewProvider {
    static var previews: some View {
        PlaybackSliderView()
    }
}
