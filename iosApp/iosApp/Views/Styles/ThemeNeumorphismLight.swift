import SwiftUI


public class ThemeNeumorphismLight: ObservableObject {
    
    @Published public var colorPalette: ColorPalette
    @Published public var style: Style
    
    public struct ColorPalette {
        public private(set) var primary: Color
        public private(set) var secondary: Color
        public private(set) var primaryVariant: Color
        
        public private(set) var textColor: Color
        public private(set) var background: Color
        public private(set) var dropShadow: Color
        public private(set) var reflection: Color
        
        public private(set) var intenseDropShadow: Color
        public private(set) var intenseReflection: Color
        
        public init() {
            self.primary = .blueGradientStart
            self.secondary = .blueGradientEnd
            self.primaryVariant = .blueGradientEnd
            self.textColor = .textColor
            self.background = .lightThemeBackground
            self.dropShadow = Color.black.opacity(0.2)
            self.reflection = Color.white.opacity(0.7)
            
            self.intenseDropShadow = Color.black.opacity(0.4)
            self.intenseReflection = Color.white
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
