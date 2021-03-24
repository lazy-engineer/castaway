import SwiftUI

struct PodcastHeaderView: View {
    
    @EnvironmentObject var theme: ThemeNeumorphismLight
    
    @State var feedImage: UIImage?
    @State var feedTitle: String
    
    var body: some View {
        HStack {
            if let imageUrl = feedImage {
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
            
            Text(feedTitle)
                .font(.title).bold().foregroundColor(theme.colorPalette.textColor)
                .minimumScaleFactor(0.7)
                .lineLimit(1)
                .padding(.trailing, 8)
            Spacer()
        }
    }
}

struct PodcastHeaderView_Previews: PreviewProvider {
    static var previews: some View {
        PodcastHeaderView(feedImage: nil, feedTitle: "Awesome Podcast")
    }
}
