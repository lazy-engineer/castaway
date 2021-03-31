import SwiftUI

struct PodcastHeaderView: View {
    
    @EnvironmentObject var theme: ThemeNeumorphism
    
    @State var feedImage: UIImage?
    @State var feedTitle: String
    
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
                        .frame(width: 150, height: 150)
                        .shadow(color: theme.colorPalette.dropShadow, radius: /*@START_MENU_TOKEN@*/10/*@END_MENU_TOKEN@*/, x: 10, y: 10)
                        .shadow(color: theme.colorPalette.reflection, radius: /*@START_MENU_TOKEN@*/10/*@END_MENU_TOKEN@*/, x: -5, y: -5)
                    
                    Image(uiImage: imageUrl)
                        .resizable()
                        .scaledToFill()
                        .frame(width: 145, height: 145)
                        .clipShape(RoundedRectangle(cornerRadius: 24.0))
                        .shadow(color: theme.colorPalette.dropShadow, radius: 10, x: 10, y: 10)
                        .shadow(color: theme.colorPalette.reflection, radius: /*@START_MENU_TOKEN@*/10/*@END_MENU_TOKEN@*/, x: -5, y: -5)
                }
            } else {
                Image(systemName: "mic")
                    .resizable()
                    .scaledToFill()
                    .frame(width: 120, height: 120)
                    .foregroundColor(theme.colorPalette.background)
                    .shadow(color: theme.colorPalette.dropShadow, radius: 5, x: 5, y: 5)
                    .shadow(color: theme.colorPalette.reflection, radius: 5, x: -5, y: -5)
                    .padding(24)
            }
        }
        .padding(.top, 24)
        .padding(.bottom, 24)
    }
}

struct PodcastHeaderView_Previews: PreviewProvider {
    static var previews: some View {
        PodcastHeaderView(feedImage: nil, feedTitle: "Awesome Podcast")
    }
}
