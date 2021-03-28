import SwiftUI

public class ThemeNeumorphism: ObservableObject {
    
    @Published public var colorPalette: ColorPalette
    @Published public var style: Style
    
    public struct ColorPalette {
        public private(set) var primary: Color
        public private(set) var secondary: Color
        public private(set) var primaryVariant: Color
        
        public private(set) var textColor: Color
        public private(set) var background: Color
        public private(set) var backgroundGradient: Color
        public private(set) var dropShadow: Color
        public private(set) var reflection: Color
        
        public private(set) var intenseDropShadow: Color
        public private(set) var intenseReflection: Color
        
        public init(_ themeMode: ThemeMode = .dark) {
            
            switch themeMode {
            case .dark:
                self.primary = .blueGradientStart
                self.secondary = .blueGradientEnd
                self.primaryVariant = .blueGradientMiddle
                self.textColor = .textColor
                self.background = .lightThemeBackground
                self.backgroundGradient = Color.gray.opacity(0.2)
                self.dropShadow = Color.black.opacity(0.2)
                self.reflection = Color.white.opacity(0.7)
                self.intenseDropShadow = Color.black.opacity(0.4)
                self.intenseReflection = Color.white
                
            case .light:
                self.primary = .blueGradientStart
                self.secondary = .blueGradientEnd
                self.primaryVariant = .blueGradientMiddle
                self.textColor = .textColor
                self.background = .lightThemeBackground
                self.backgroundGradient = Color.gray.opacity(0.2)
                self.dropShadow = Color.black.opacity(0.2)
                self.reflection = Color.white.opacity(0.7)
                self.intenseDropShadow = Color.black.opacity(0.4)
                self.intenseReflection = Color.white
            }
        }
    }
    
    public struct Style {
        public private(set) var roundButtonStyle: NeumorphismCircleButtonStyle
        public private(set) var pillButtonStyle: NeumorphismPillStyle
        public private(set) var underlineButtonStyle: NeumorphismUnderlineStyle
        public private(set) var roundToggleButtonStyle: NeumorphismCircleToggleStyle
        
        public init(_ themeMode: ThemeMode = .dark) {
            self.roundButtonStyle = NeumorphismCircleButtonStyle(themeMode: themeMode)
            self.pillButtonStyle = NeumorphismPillStyle(themeMode: themeMode)
            self.underlineButtonStyle = NeumorphismUnderlineStyle(themeMode: themeMode)
            self.roundToggleButtonStyle = NeumorphismCircleToggleStyle(themeMode: themeMode)
        }
    }
    
    public init(mode: ThemeMode = .dark) {
        switch mode {
        case .dark:
            self.colorPalette = ColorPalette(.dark)
            self.style = Style(.dark)
        case .light:
            self.colorPalette = ColorPalette(.light)
            self.style = Style(.light)
        }
    }
}
