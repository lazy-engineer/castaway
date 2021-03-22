import SwiftUI

struct StartScreen : View {
    
    @EnvironmentObject var theme: ThemeNeumorphismLight
    @EnvironmentObject var viewModel: CastawayViewModel
    
    var body: some View {
        VStack {
            if viewModel.episodes.isEmpty {
                LoadingIndicatorView().onAppear() {
                    viewModel.loadFeed("https://atp.fm/rss")
                }.background(theme.colorPalette.background)
            } else {
                
                HStack {
                    if let imageUrl = viewModel.feedImage {
                        Image(uiImage: imageUrl)
                            .resizable()
                            .scaledToFill()
                            .frame(width: 48, height: 48)
                            .cornerRadius(10)
                            .padding(8)
                    } else {
                        Image(systemName: "mic")
                            .frame(width: 48, height: 48)
                            .foregroundColor(Color.white)
                            .background(Color.black)
                            .clipShape(Rectangle())
                            .cornerRadius(10)
                            .padding(8)
                    }
                    
                    Text(viewModel.feedTitle)
                        .font(.title)
                        .minimumScaleFactor(0.7)
                        .lineLimit(1)
                        .padding(.trailing, 8)
                    Spacer()
                }
                
                EpisodeListView()
                    .environmentObject(viewModel)
                    .environmentObject(theme)
            }
        }.background(theme.colorPalette.background.edgesIgnoringSafeArea(.all))
    }
}

#if DEBUG
struct StartScreen_Previews: PreviewProvider {
    static var previews: some View {
        StartScreen()
    }
}
#endif


struct ContentView: View {
    
    @State private var isToggled = false
    
    var body: some View {
        ZStack {
            Color.lightThemeBackground
            
            VStack {
                RoundedRectangle(cornerRadius: 25.0)
                    .fill(Color.lightThemeBackground)
                    .frame(width: 180, height: 4)
                    .shadow(color: Color.black.opacity(0.4), radius: 3, x: 5, y: 3)
                    .shadow(color: Color.white, radius: 4, x: -2, y: -2)
                    .padding(.top, 64)
                    .padding(.bottom, 48)
                
                RoundedRectangle(cornerRadius: 25.0)
                    .fill(Color.lightThemeBackground)
                    .frame(width: 300, height: 300)
                    .shadow(color: Color.black.opacity(0.2), radius: /*@START_MENU_TOKEN@*/10/*@END_MENU_TOKEN@*/, x: 10, y: 10)
                    .shadow(color: Color.white.opacity(0.7), radius: /*@START_MENU_TOKEN@*/10/*@END_MENU_TOKEN@*/, x: -5, y: -5)
                
                HStack {
                    Button(action: {
                        
                    }) {
                        Image(systemName: "gobackward.30")
                            .foregroundColor(.blueGradientStart)
                            .frame(width: 60, height: 60)
                    }
                    .buttonStyle(ColorfulButtonStyle())
                    .padding(8)
                    
                    Toggle(isOn: $isToggled) {
                        Image(systemName: isToggled ? "pause.fill" : "play.fill")
                            .foregroundColor(isToggled ? .white : .blueGradientStart)
                            .frame(width: 80, height: 80)
                    }
                    .toggleStyle(ColorfulToggleStyle())
                    .padding(8)
                    
                    Button(action: {
                        
                    }) {
                        Image(systemName: "goforward.30")
                            .foregroundColor(.blueGradientStart)
                            .frame(width: 60, height: 60)
                    }
                    .buttonStyle(ColorfulButtonStyle())
                    .padding(8)
                    
                }.padding(.top, 48)
                
                Spacer()
            }
        }
        .edgesIgnoringSafeArea(.all)
    }
}
