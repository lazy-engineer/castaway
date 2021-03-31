import SwiftUI

struct StartScreen : View {
    
    @EnvironmentObject var theme: ThemeNeumorphism
    @EnvironmentObject var viewModel: CastawayViewModel
    
    var body: some View {
        VStack {
            if viewModel.episodes.isEmpty {
                LoadingIndicatorView().onAppear() {
                    viewModel.loadFeed("https://atp.fm/rss")
                }
                .environmentObject(theme)
            } else {
                ScrollView {
                    PodcastHeaderView(feedImage: viewModel.feedImage, feedTitle: viewModel.feedTitle)
                        .environmentObject(theme)
                    
                    EpisodeListView()
                        .environmentObject(viewModel)
                        .environmentObject(theme)
                }
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
