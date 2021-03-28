import SwiftUI

struct PlaybackSliderView: View {
    
    @EnvironmentObject var theme: ThemeNeumorphism
    @EnvironmentObject var viewModel: CastawayViewModel
    @State private var playbackPosition: TimeInterval = 0
    @State private var duration: TimeInterval = 1
    
    @State private var playbackOffset: CGFloat = 0
    @State private var playbackProgress: CGFloat = 0
    private let offsetPadding: CGFloat = 20
    private let positionCircleSize: CGFloat = 5
    
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
            }.padding(.bottom, 8)
            
            ZStack(alignment: Alignment(horizontal: .leading, vertical: .top), content: {
                PlaybackProgressView(
                    playbackProgress: playbackProgress,
                    offsetPadding: 20)
                
                Circle()
                    .fill(theme.colorPalette.primary)
                    .frame(width: positionCircleSize, height: positionCircleSize)
                    .background(
                        Circle()
                            .fill(theme.colorPalette.background)
                            .frame(width: 25, height: 25)
                            .overlay(Circle().stroke(LinearGradient(theme.colorPalette.background, theme.colorPalette.backgroundGradient), lineWidth: 3))
                            .overlay(
                                Circle()
                                    .stroke(theme.colorPalette.dropShadow, lineWidth: 2)
                                    .blur(radius: 4)
                                    .offset(x: 2, y: 2)
                                    .mask(Circle().fill(LinearGradient(Color.black, Color.clear)))
                            )
                            .overlay(
                                Circle()
                                    .stroke(theme.colorPalette.reflection, lineWidth: 4)
                                    .blur(radius: 4)
                                    .offset(x: -2, y: -2)
                                    .mask(Circle().fill(LinearGradient(Color.clear, Color.black)))
                            )
                            .shadow(color: theme.colorPalette.dropShadow, radius: 2, x: 1, y: 2)
                            .shadow(color: theme.colorPalette.reflection, radius: 2, x: -1, y: -2)
                        
                    )
                    .offset(x: playbackOffset + offsetPadding)
                    .gesture(
                        DragGesture()
                            .onChanged({ value in
                                sliderEditingChanged(editingStarted: true)
                                
                                if value.location.x >= offsetPadding && value.location.x <= UIScreen.main.bounds.width - (offsetPadding + positionCircleSize) {
                                    playbackOffset = value.location.x - offsetPadding
                                    let time = CGFloat(duration) * (playbackOffset/(UIScreen.main.bounds.width - (offsetPadding * 2 + positionCircleSize)))
                                    
                                    playbackPosition = TimeInterval(time)
                                    playbackProgress = CGFloat(playbackPosition / duration)
                                }
                            })
                            .onEnded({ value in
                                sliderEditingChanged(editingStarted: false)
                            }))
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
                        playbackOffset = CGFloat(playbackPosition / duration)
                            * (UIScreen.main.bounds.width - (offsetPadding * 2 + positionCircleSize))
                        playbackProgress = CGFloat(playbackPosition / duration)
                    }
            })
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
