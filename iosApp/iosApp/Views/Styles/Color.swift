import SwiftUI

extension Color {
    static let lightThemeBackground = Color(red: 225 / 255, green: 234 / 255, blue: 252 / 255)
    static let darkThemeBackground = Color(red: 41 / 255, green: 45 / 255, blue: 50 / 255)
    static let textColor = Color(red: 102 / 255, green: 112 / 255, blue: 130 / 255)
    static let darkThemeTextColor = Color(red: 189 / 255, green: 189 / 255, blue: 189 / 255)
    
    static let lightThemeDarkShadow = Color.black.opacity(0.2)
    static let lightThemeLightShadow = Color.white.opacity(0.7)
    
    static let darkThemeDarkShadow = Color.black.opacity(0.4)
    static let darkThemeLightShadow = Color(red: 48 / 255, green: 52 / 255, blue: 58 / 255)
    
    static let darkGradientStart = Color(red: 50 / 255, green: 60 / 255, blue: 65 / 255)
    static let darkGradientEnd = Color(red: 25 / 255, green: 25 / 255, blue: 30 / 255)

    static let orangeGradientStart = Color(red: 255 / 255, green: 98 / 255, blue: 0 / 255)
    static let orangeGradientEnd = Color(red: 180 / 255, green: 49 / 255, blue: 0 / 255)
    static let orangeGradientMiddle = Color(red: 255 / 255, green: 180 / 255, blue: 0 / 255)
    
    static let blueGradientStart = Color(red: 66 / 255, green: 165 / 255, blue: 245 / 255)
    static let blueGradientEnd = Color(red: 13 / 255, green: 71 / 255, blue: 161 / 255)
    static let blueGradientMiddle = Color(red: 25 / 255, green: 118 / 255, blue: 210 / 255)
    
    static let darkGray = Color(red: 97 / 255, green: 97 / 255, blue: 97 / 255)
}

extension LinearGradient {
    init(_ colors: Color...) {
        self.init(gradient: Gradient(colors: colors), startPoint: .topLeading, endPoint: .bottomTrailing)
    }
}
