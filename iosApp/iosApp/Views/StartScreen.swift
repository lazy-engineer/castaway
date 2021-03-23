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
