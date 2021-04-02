import SwiftUI

struct StartScreen : View {
    
    @EnvironmentObject var theme: ThemeNeumorphism
    @EnvironmentObject var viewModel: CastawayViewModel
    
    @State var showOnboarding = true
        
    var body: some View {
        ZStack {
            PodcastScreen()
                .environmentObject(viewModel)
                .environmentObject(theme)
            
            if showOnboarding {
                OnBoardingScreen() { finished in
                    showOnboarding = !finished
                }
                .environmentObject(theme)
            }
        }
    }
}

#if DEBUG
struct StartScreen_Previews: PreviewProvider {
    static var previews: some View {
        StartScreen()
    }
}
#endif
