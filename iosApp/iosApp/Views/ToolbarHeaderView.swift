import SwiftUI

struct ToolbarHeaderView: View {
    
    @EnvironmentObject var theme: ThemeNeumorphism
    
    @State var feedImage: UIImage?
    @State var feedTitle: String
    
    var body: some View {
        VStack(spacing: 0) {
            HStack {
                if let imageUrl = feedImage {
                    ZStack {
                        RoundedRectangle(cornerRadius: 25.0)
                            .fill(theme.colorPalette.background)
                            .frame(width: 50, height: 50)
                            .shadow(color: theme.colorPalette.dropShadow, radius: 5, x: 5, y: 5)
                            .shadow(color: theme.colorPalette.reflection, radius: 5, x: -5, y: -5)
                        
                        Image(uiImage: imageUrl)
                            .resizable()
                            .scaledToFill()
                            .frame(width: 45, height: 45)
                            .clipShape(RoundedRectangle(cornerRadius: 24.0))
                            .shadow(color: theme.colorPalette.dropShadow, radius: 5, x: 5, y: 5)
                            .shadow(color: theme.colorPalette.reflection, radius: 5, x: -5, y: -5)
                    }
                    .padding()
                } else {
                    Image(systemName: "mic")
                        .resizable()
                        .scaledToFill()
                        .frame(width: 32, height: 32)
                        .foregroundColor(theme.colorPalette.background)
                        .shadow(color: theme.colorPalette.dropShadow, radius: 5, x: 5, y: 5)
                        .shadow(color: theme.colorPalette.reflection, radius: 5, x: -5, y: -5)
                        .padding(8)
                }
                
                Text(feedTitle)
                    .font(.title2).bold().foregroundColor(theme.colorPalette.textColor)
                    .minimumScaleFactor(0.7)
                    .lineLimit(1)
                    .frame(maxWidth: .infinity, alignment: .leading)
                    .padding(.trailing, 16)
            }
            .background(theme.colorPalette.background.edgesIgnoringSafeArea(.top))
            
            Rectangle()
                .foregroundColor(.clear)
                .background(LinearGradient.init(gradient:
                                                    Gradient(colors: [
                                                                theme.colorPalette.background,
                                                                theme.colorPalette.background.opacity(0.9),
                                                                theme.colorPalette.background.opacity(0.8),
                                                                theme.colorPalette.background.opacity(0.7),
                                                                theme.colorPalette.background.opacity(0.6),
                                                                theme.colorPalette.background.opacity(0.3),
                                                                theme.colorPalette.background.opacity(0)]),
                                                startPoint: .top, endPoint: .bottom))
                .frame(height: 50)
        }
    }
}
struct ToolbarHeaderView_Previews: PreviewProvider {
    static var previews: some View {
        ToolbarHeaderView(feedImage: nil, feedTitle: "Awesome Podcast")
            .environmentObject(ThemeNeumorphism(mode: .light))
    }
}
