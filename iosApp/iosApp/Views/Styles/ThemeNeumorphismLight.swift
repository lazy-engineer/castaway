import SwiftUI


public class ThemeNeumorphismLight: ObservableObject {
    
    @Published public var colorPalette: ColorPalette
    @Published public var style: Style
    
    public struct ColorPalette {
        public private(set) var primary: Color
        public private(set) var secondary: Color
        public private(set) var primaryVariant: Color
        
        public private(set) var background: Color
        public private(set) var darkShadow: Color
        public private(set) var lightShadow: Color
        
        public private(set) var darkerShadow: Color
        public private(set) var lighterShadow: Color
        
        public init() {
            self.primary = .blueGradientStart
            self.secondary = .blueGradientEnd
            self.primaryVariant = .blueGradientEnd
            self.background = .lightThemeBackground
            self.darkShadow = Color.black.opacity(0.2)
            self.lightShadow = Color.white.opacity(0.7)
            
            self.darkerShadow = Color.black.opacity(0.4)
            self.lighterShadow = Color.white
        }
    }
    
    public struct Style {
        public private(set) var roundButtonStyle: ColorfulButtonStyle
        public private(set) var roundToggleButtonStyle: ColorfulToggleStyle
        
        public init() {
            self.roundButtonStyle = ColorfulButtonStyle()
            self.roundToggleButtonStyle = ColorfulToggleStyle()
        }
    }
    
    public init(colorPalette: ColorPalette = ColorPalette(), style: Style = Style()) {
        self.colorPalette = colorPalette
        self.style = style
    }
}
