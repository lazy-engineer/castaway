import SwiftUI

struct PlaybackSliderView: View {
    
    @EnvironmentObject var theme: ThemeNeumorphismLight
    @EnvironmentObject var viewModel: CastawayViewModel
    @State private var playbackPosition: TimeInterval = 0
    @State private var duration: TimeInterval = 1
    
    @State private var offset: CGFloat = 0
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
            }
            
            ZStack(alignment: Alignment(horizontal: .leading, vertical: .center), content: {
                Capsule()
                    .fill(theme.colorPalette.background)
                    .overlay(
                        Capsule()
                            .stroke(theme.colorPalette.intenseDropShadow, lineWidth: 2)
                            .blur(radius: 2)
                            .mask(Capsule().fill(LinearGradient.init(gradient: Gradient(colors: [Color.black, Color.clear]), startPoint: .top, endPoint: .bottom)))
                    )
                    .overlay(
                        Capsule()
                            .stroke(theme.colorPalette.intenseReflection, lineWidth: 3)
                            .offset(y: -1)
                            .blur(radius: 1)
                            .mask(Capsule().fill(LinearGradient.init(gradient: Gradient(colors: [Color.black, Color.clear]), startPoint: .bottom, endPoint: .top)))
                    )
                    .frame(height: 5)
                    .padding(.leading, offsetPadding)
                    .padding(.trailing, offsetPadding)
                
                Capsule()
                    .fill(LinearGradient(theme.colorPalette.primaryVariant, theme.colorPalette.primary))
                    .frame(width: offset + positionCircleSize, height: 5)
                    .padding(.leading, offsetPadding)
                    .padding(.trailing, offsetPadding)
                
                Circle()
                    .fill(theme.colorPalette.primary)
                    .frame(width: positionCircleSize, height: positionCircleSize)
                    .background(Circle().stroke(Color.white, lineWidth: 24))
                    .offset(x: offset + offsetPadding)
                    .gesture(
                        DragGesture()
                            .onChanged({ value in
                                sliderEditingChanged(editingStarted: true)
                                
                                if value.location.x >= offsetPadding && value.location.x <= UIScreen.main.bounds.width - (offsetPadding + positionCircleSize) {
                                    offset = value.location.x - offsetPadding
                                    let time = CGFloat(duration) * (offset/(UIScreen.main.bounds.width - (offsetPadding * 2 + positionCircleSize)))
                                    playbackPosition = TimeInterval(time)
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
                        offset = CGFloat(playbackPosition / duration) * (UIScreen.main.bounds.width - (offsetPadding * 2 + positionCircleSize))
                    }
            })
            .padding(.top, 10)
            .padding(.bottom, 10)
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
