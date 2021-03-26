import SwiftUI

struct NowPlayingScreen: View {
    
    @Environment(\.presentationMode) var presentationMode
    
    @EnvironmentObject var theme: ThemeNeumorphismLight
    @EnvironmentObject var viewModel: CastawayViewModel
    
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
                        .shadow(color: theme.colorPalette.intenseDropShadow, radius: 3, x: 5, y: 3)
                        .shadow(color: theme.colorPalette.intenseReflection, radius: 4, x: -2, y: -2)
                        .padding(.top, 64)
                        .padding(.bottom, 48)
                }
                
                if let imageUrl = viewModel.feedImage {
                    ZStack {
                        RoundedRectangle(cornerRadius: 25.0)
                            .fill(theme.colorPalette.background)
                            .frame(width: 300, height: 300)
                            .shadow(color: theme.colorPalette.dropShadow, radius: /*@START_MENU_TOKEN@*/10/*@END_MENU_TOKEN@*/, x: 10, y: 10)
                            .shadow(color: theme.colorPalette.reflection, radius: /*@START_MENU_TOKEN@*/10/*@END_MENU_TOKEN@*/, x: -5, y: -5)

                        Image(uiImage: imageUrl)
                            .resizable()
                            .scaledToFill()
                            .frame(width: 292, height: 292)
                            .clipShape(RoundedRectangle(cornerRadius: 25.0))
                            .shadow(color: theme.colorPalette.dropShadow, radius: /*@START_MENU_TOKEN@*/10/*@END_MENU_TOKEN@*/, x: 10, y: 10)
                            .shadow(color: theme.colorPalette.reflection, radius: /*@START_MENU_TOKEN@*/10/*@END_MENU_TOKEN@*/, x: -5, y: -5)
                    }
                } else {
                    Image(systemName: "mic")
                        .resizable()
                        .scaledToFill()
                        .frame(width: 200, height: 250)
                        .foregroundColor(theme.colorPalette.background)
                        .shadow(color: theme.colorPalette.dropShadow, radius: /*@START_MENU_TOKEN@*/10/*@END_MENU_TOKEN@*/, x: 10, y: 10)
                        .shadow(color: theme.colorPalette.reflection, radius: /*@START_MENU_TOKEN@*/10/*@END_MENU_TOKEN@*/, x: -5, y: -5)
                        .padding(24)
                }
                
                Text(viewModel.currentEpisode?.title ?? "")
                    .font(.headline).bold().foregroundColor(theme.colorPalette.textColor)
                    .padding(.top, 24)
                    .fixedSize(horizontal: false, vertical: true)
                    .multilineTextAlignment(.center)
                
                HStack(spacing: 24) {
                    Spacer()
      
                    Button(action: {
                        viewModel.replayCurrentItem()
                    }) {
                        Image(systemName: "gobackward.30")
                            .frame(width: 20, height: 20)
                    }
                    .buttonStyle(theme.style.roundButtonStyle)
                    
                    Toggle(isOn: $viewModel.playing) {
                        Image(systemName: viewModel.playing ? "pause.fill" : "play.fill")
                            .foregroundColor(viewModel.playing ? .white : theme.colorPalette.primary)
                            .frame(width: 50, height: 50)
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
                            .frame(width: 20, height: 20)
                    }
                    .buttonStyle(theme.style.roundButtonStyle)
                    
                    Spacer()
                }
                .padding(16)
                
                PlaybackSliderView()
                    .environmentObject(viewModel)
                    .environmentObject(theme)

                HStack {
                    Button(action: {
                        viewModel.changePlaybackSpeed()
                    }) {
                        Text("\(String(format: "%.1f", viewModel.playbackSpeed))x")
                            .foregroundColor(theme.colorPalette.primary).bold()
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
}

#if DEBUG
struct NowPlayingScreen_Previews: PreviewProvider {
    static var previews: some View {
        NowPlayingScreen().environmentObject(ThemeNeumorphismLight())
    }
}
#endif
