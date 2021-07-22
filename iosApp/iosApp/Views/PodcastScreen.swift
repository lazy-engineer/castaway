import SwiftUI

struct PodcastScreen: View {
    
    @EnvironmentObject var theme: ThemeNeumorphism
    @EnvironmentObject var viewModel: CastawayViewModel
    
    @State var showToolbarHeader = false
    
    var body: some View {
        VStack {
            if viewModel.episodes.isEmpty {
                LoadingIndicatorView().onAppear() {
                    viewModel.loadFeed("https://atp.fm/rss")
                }
                .environmentObject(theme)
            } else {
                ZStack(alignment: .top, content: {
                    
                    ScrollView {
                        PodcastHeaderView(feedImage: viewModel.feedImage, feedTitle: viewModel.feedTitle) { outOfSight in
                            withAnimation {
                                showToolbarHeader = outOfSight
                            }
                        }
                        .environmentObject(theme)
                        
                        EpisodeListView()
                            .environmentObject(viewModel)
                            .environmentObject(theme)
                    }
                    
                    if showToolbarHeader {
                        ToolbarHeaderView(feedTitle: viewModel.feedTitle, feedImage: viewModel.feedImage)
                            .environmentObject(theme)
                            .transition(.move(edge: .top))
                            .animation(.easeInOut(duration: 0.5))
                            .zIndex(1.0)
                    }
                })
            }
        }
        .background(theme.colorPalette.background.edgesIgnoringSafeArea(.all))
    }
}

#if DEBUG
struct PodcastScreen_Previews: PreviewProvider {
    static var previews: some View {
        PodcastScreen()
    }
}
#endif
