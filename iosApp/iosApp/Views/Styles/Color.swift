import SwiftUI

extension Color {
    static let lightThemeBackground = Color(red: 225 / 255, green: 234 / 255, blue: 252 / 255)
    static let textColor = Color(red: 102 / 255, green: 112 / 255, blue: 130 / 255)
    
    static let lightThemeDarkShadow = Color.black.opacity(0.2)
    static let lightThemeLightShadow = Color.white.opacity(0.7)
    
    static let darkGradientStart = Color(red: 50 / 255, green: 60 / 255, blue: 65 / 255)
    static let darkGradientEnd = Color(red: 25 / 255, green: 25 / 255, blue: 30 / 255)
    
    static let blueGradientStart = Color(red: 66 / 255, green: 165 / 255, blue: 245 / 255)
    static let blueGradientEnd = Color(red: 13 / 255, green: 71 / 255, blue: 161 / 255)
}

extension LinearGradient {
    init(_ colors: Color...) {
        self.init(gradient: Gradient(colors: colors), startPoint: .topLeading, endPoint: .bottomTrailing)
    }
}
