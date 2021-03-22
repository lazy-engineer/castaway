import SwiftUI

struct NowPlayingScreen: View {
    
    @Environment(\.presentationMode) var presentationMode
    
    @EnvironmentObject var theme: ThemeNeumorphismLight
    @EnvironmentObject var viewModel: CastawayViewModel
    @State private var playbackPosition: TimeInterval = 0
    @State private var duration: TimeInterval = 1
    
    var body: some View {
        ZStack {
            theme.colorPalette.background
            
            VStack {
                
                Button(action: {
                    presentationMode.wrappedValue.dismiss()
                }) {
                    RoundedRectangle(cornerRadius: 25.0)
                        .fill(theme.colorPalette.background)
                        .frame(width: 180, height: 4)
                        .shadow(color: theme.colorPalette.darkerShadow, radius: 3, x: 5, y: 3)
                        .shadow(color: theme.colorPalette.lighterShadow, radius: 4, x: -2, y: -2)
                        .padding(.top, 64)
                        .padding(.bottom, 48)
                }
                
                if let imageUrl = viewModel.feedImage {
                    ZStack {
                        RoundedRectangle(cornerRadius: 25.0)
                            .fill(theme.colorPalette.background)
                            .frame(width: 300, height: 300)
                            .shadow(color: theme.colorPalette.darkShadow, radius: /*@START_MENU_TOKEN@*/10/*@END_MENU_TOKEN@*/, x: 10, y: 10)
                            .shadow(color: theme.colorPalette.lightShadow, radius: /*@START_MENU_TOKEN@*/10/*@END_MENU_TOKEN@*/, x: -5, y: -5)
                        
                        Image(uiImage: imageUrl)
                            .resizable()
                            .scaledToFill()
                            .frame(width: 292, height: 292)
                            .clipShape(RoundedRectangle(cornerRadius: 25.0))
                            .shadow(color: theme.colorPalette.darkShadow, radius: /*@START_MENU_TOKEN@*/10/*@END_MENU_TOKEN@*/, x: 10, y: 10)
                            .shadow(color: theme.colorPalette.lightShadow, radius: /*@START_MENU_TOKEN@*/10/*@END_MENU_TOKEN@*/, x: -5, y: -5)
                    }
                } else {
                    Image(systemName: "mic")
                        .resizable()
                        .scaledToFill()
                        .frame(width: 200, height: 250)
                        .foregroundColor(theme.colorPalette.background)
                        .clipShape(RoundedRectangle(cornerRadius: 25))
                        .shadow(color: theme.colorPalette.darkShadow, radius: /*@START_MENU_TOKEN@*/10/*@END_MENU_TOKEN@*/, x: 10, y: 10)
                        .shadow(color: theme.colorPalette.lightShadow, radius: /*@START_MENU_TOKEN@*/10/*@END_MENU_TOKEN@*/, x: -5, y: -5)
                }
                
                Text(viewModel.currentEpisode?.title ?? "")
                    .padding(.top, 24)
                    .fixedSize(horizontal: false, vertical: true)
                    .multilineTextAlignment(.center)
                
                HStack(spacing: 24) {
                    Spacer()
                    
                    Button(action: {
                        viewModel.replayCurrentItem()
                    }) {
                        Image(systemName: "gobackward.30")
                            .frame(width: 60, height: 60)
                            .foregroundColor(theme.colorPalette.primary)
                    }
                    .buttonStyle(theme.style.roundButtonStyle)
                    
                    Toggle(isOn: $viewModel.playing) {
                        Image(systemName: viewModel.playing ? "pause.fill" : "play.fill")
                            .foregroundColor(viewModel.playing ? .white : theme.colorPalette.primary)
                            .frame(width: 80, height: 80)
                    }
                    .onChange(of: viewModel.playing, perform: { value in
                        viewModel.playPauseCurrent(playState: viewModel.playing)
                    })
                    .toggleStyle(theme.style.roundToggleButtonStyle)
                    .padding(16)
                    
                    Button(action: {
                        viewModel.forwardCurrentItem()
                    }) {
                        Image(systemName: "goforward.30")
                            .frame(width: 60, height: 60)
                            .foregroundColor(theme.colorPalette.primary)
                    }
                    .buttonStyle(theme.style.roundButtonStyle)
                    
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
                            .foregroundColor(theme.colorPalette.primary)
                    }
                    .padding(.leading, 24)
                    .padding(.bottom, 24)
                    .padding(.top, 8)
                    
                    Spacer()
                }
                
                Spacer()
            }
        }.edgesIgnoringSafeArea(.all)
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
