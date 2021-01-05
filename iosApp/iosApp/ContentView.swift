import SwiftUI
import shared

func greet() -> String {
    return Greeting().greeting()
}

struct ContentView: View {
    
    var viewModel = CastawayViewModel(getFeedUseCase: GetFeedUseCase())
    
    var body: some View {
        List {
            Text(greet())
            Text(viewModel.fetchFeed())
        }
    }
}

struct ContentView_Previews: PreviewProvider {
    static var previews: some View {
        ContentView()
    }
}
