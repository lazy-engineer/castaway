import SwiftUI

struct PlaybackProgressView: View {
    
    @EnvironmentObject var theme: ThemeNeumorphism
    
    let playbackProgress: CGFloat
    let offsetPadding: CGFloat
    
    public init(
        playbackProgress: CGFloat,
        offsetPadding: CGFloat = 0
    ) {
        self.playbackProgress = playbackProgress
        self.offsetPadding = offsetPadding
    }
    
    var body: some View {
        ZStack(alignment: Alignment(horizontal: .leading, vertical: .center), content: {
            GeometryReader { geometry in
                Capsule()
                    .fill(theme.colorPalette.background)
                    .frame(height: 5)
                    .overlay(
                        Capsule()
                            .stroke(theme.colorPalette.intenseDropShadow, lineWidth: 3)
                            .blur(radius: 2)
                            .mask(Capsule().fill(LinearGradient.init(gradient: Gradient(colors: [Color.black, Color.clear]), startPoint: .top, endPoint: .bottom)))
                    )
                    .overlay(
                        Capsule()
                            .stroke(theme.colorPalette.intenseReflection, lineWidth: 1)
                            .offset(y: -1)
                            .blur(radius: 1)
                            .mask(Capsule().fill(LinearGradient.init(gradient: Gradient(colors: [Color.black, Color.clear]), startPoint: .bottom, endPoint: .top)))
                    )
                    .padding(.leading, offsetPadding)
                    .padding(.trailing, offsetPadding)
                
                Capsule()
                    .fill(LinearGradient(theme.colorPalette.primaryVariant, theme.colorPalette.primary))
                    .frame(width: playbackProgress > 1 ? (geometry.size.width - offsetPadding * 2) * 1 : (geometry.size.width - offsetPadding * 2) * playbackProgress, height: 5)
                    .padding(.leading, offsetPadding)
                    .padding(.trailing, offsetPadding)
            }
        })
    }
}

#if DEBUG
struct PlaybackProgressView_Previews: PreviewProvider {
    static var previews: some View {
        PlaybackProgressView(playbackProgress: 0.25)
            .environmentObject(ThemeNeumorphism())
    }
}
#endif
