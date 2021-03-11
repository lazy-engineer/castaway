import SwiftUI

struct LoadingIndicatorView : View {
    
    @State private var dots: String = ""
    private let timer = Timer.publish(every: 1, on: .main, in: .common).autoconnect()
    
    var body: some View {
        Text("Loading \(dots)")
            .font(.title2)
            .onReceive(timer, perform: { _ in
                if self.dots.count == 3 {
                    self.dots = ""
                } else {
                    self.dots += "."
                }
            })
    }
}

struct LoadingIndicatorView_Previews: PreviewProvider {
    static var previews: some View {
        LoadingIndicatorView()
    }
}
