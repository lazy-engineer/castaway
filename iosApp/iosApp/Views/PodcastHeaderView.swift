import SwiftUI

struct PodcastHeaderView: View {
    
    @EnvironmentObject var theme: ThemeNeumorphism
    
    @State var time = Timer.publish(every: 0.1, on: .current, in: .tracking).autoconnect()
    @State var size: CGFloat = 150
    
    @State var feedImage: UIImage?
    @State var feedTitle: String
    let outOfSight: (Bool) -> Void
    
    var body: some View {
        VStack {            
            Text(feedTitle)
                .font(.title2).bold().foregroundColor(theme.colorPalette.textColor)
                .minimumScaleFactor(0.7)
                .lineLimit(1)
                .padding(.bottom, 16)
            
            if let imageUrl = feedImage {
                ZStack {
                    RoundedRectangle(cornerRadius: 25.0)
                        .fill(theme.colorPalette.background)
                        .frame(width: size, height: size)
                        .shadow(color: theme.colorPalette.dropShadow, radius: /*@START_MENU_TOKEN@*/10/*@END_MENU_TOKEN@*/, x: 10, y: 10)
                        .shadow(color: theme.colorPalette.reflection, radius: /*@START_MENU_TOKEN@*/10/*@END_MENU_TOKEN@*/, x: -5, y: -5)

                    GeometryReader { geometry in
                        Image(uiImage: imageUrl)
                            .resizable()
                            .scaledToFill()
                            .onReceive(self.time, perform: { _ in
                                scaleOnScroll(geometry.frame(in: .global).minY, maxImageSize: 150)
                            })
                    }
                    .frame(width: size - 5, height: size - 5)
                    .clipShape(RoundedRectangle(cornerRadius: 24.0))
                    .shadow(color: theme.colorPalette.dropShadow, radius: 10, x: 10, y: 10)
                    .shadow(color: theme.colorPalette.reflection, radius: /*@START_MENU_TOKEN@*/10/*@END_MENU_TOKEN@*/, x: -5, y: -5)

                }
                .frame(width: size, height: size)
            } else {
                GeometryReader { geometry in
                    Image(systemName: "mic")
                        .resizable()
                        .scaledToFill()
                        .onReceive(self.time, perform: { _ in
                            scaleOnScroll(geometry.frame(in: .global).minY, maxImageSize: 80)
                        })
                        .onAppear {
                            size = 80
                        }
                }
                .frame(width: size, height: size)
                .foregroundColor(theme.colorPalette.background)
                .shadow(color: theme.colorPalette.dropShadow, radius: 5, x: 5, y: 5)
                .shadow(color: theme.colorPalette.reflection, radius: 5, x: -5, y: -5)
                .padding(.bottom, 24)
            }
        }
        .padding(.top, 24)
        .padding(.bottom, 24)
    }
    
    
    fileprivate func scaleOnScroll(_ geometryMinY: CGFloat, maxImageSize: CGFloat) {
        let minY: CGFloat = 0
        let maxY: CGFloat = 130
        let minSize: CGFloat = 50
        let maxSize: CGFloat = maxImageSize
        
        withAnimation {
            if geometryMinY < -minSize {
                outOfSight(true)
            } else {
                outOfSight(false)
            }
        }
        
        if geometryMinY > minY && geometryMinY < maxY  {
            let sizeOffset = maxSize - minSize
            let yOffset = maxY - minY
            let factor = sizeOffset / yOffset
            
            withAnimation {
                let newSize = geometryMinY * factor + minSize
                
                if newSize > minSize && newSize < maxSize {
                    self.size = newSize
                }
            }
        }
    }
}

#if DEBUG
struct PodcastHeaderView_Previews: PreviewProvider {
    static var previews: some View {
        PodcastHeaderView(feedImage: nil, feedTitle: "Awesome Podcast") { _ in }
            .environmentObject(ThemeNeumorphism(mode: .light))
    }
}
#endif
