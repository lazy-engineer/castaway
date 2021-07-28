import SwiftUI
import shared

public class ThemeNeumorphism: ObservableObject {
    
    @Published public var mode: ThemeMode
    @Published public var colorPalette: ColorPalette
    @Published public var style: Style
    
    public init(mode: ThemeMode = .dark) {
        self.mode = mode
        self.colorPalette = ColorPalette(mode)
        self.style = Style(mode)
    }
    
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
        
        public init(_ themeMode: ThemeMode = .dark, _ colors: Colors = Colors.init()) {
            
            switch themeMode {
            case .dark:
                self.primary = colors.orangeGradientStart.toColor()
                self.secondary = colors.orangeGradientEnd.toColor()
                self.primaryVariant = colors.orangeGradientMiddle.toColor()
                self.textColor = colors.darkThemeTextColor.toColor()
                self.background = colors.darkThemeBackground.toColor()
                self.backgroundGradient = colors.darkThemeDarkShadow.toColor()
                self.dropShadow = colors.darkThemeDarkShadow.toColor()
                self.reflection = colors.darkThemeLightShadow.toColor()
                self.intenseDropShadow = colors.darkThemeIntenseDropShadow.toColor()
                self.intenseReflection = colors.darkGray.toColor()
                
            case .light:
                self.primary = colors.blueGradientStart.toColor()
                self.secondary = colors.blueGradientEnd.toColor()
                self.primaryVariant = colors.blueGradientMiddle.toColor()
                self.textColor = colors.lightThemeTextColor.toColor()
                self.background = colors.lightThemeBackground.toColor()
                self.backgroundGradient = colors.lightThemeBackgroundGradient.toColor()
                self.dropShadow = colors.lightThemeDarkShadow.toColor()
                self.reflection = colors.lightThemeLightShadow.toColor()
                self.intenseDropShadow = colors.darkThemeDarkShadow.toColor()
                self.intenseReflection = colors.white.toColor()
            }
        }
    }
    
    public struct Style {
        public private(set) var roundButtonStyle: NeumorphismCircleButtonStyle
        public private(set) var pillButtonStyle: NeumorphismPillStyle
        public private(set) var underlineButtonStyle: NeumorphismUnderlineStyle
        public private(set) var roundToggleButtonStyle: NeumorphismCircleToggleStyle
        public private(set) var textToggleStyle: NeumorphismTextToggleStyle
        
        public init(_ themeMode: ThemeMode = .dark) {
            self.roundButtonStyle = NeumorphismCircleButtonStyle(themeMode: themeMode)
            self.pillButtonStyle = NeumorphismPillStyle(themeMode: themeMode)
            self.underlineButtonStyle = NeumorphismUnderlineStyle(themeMode: themeMode)
            self.roundToggleButtonStyle = NeumorphismCircleToggleStyle(themeMode: themeMode)
            self.textToggleStyle = NeumorphismTextToggleStyle(themeMode: themeMode)
        }
    }
    
    func changeThemeMode(mode: ThemeMode) {
        self.mode = mode
        self.colorPalette = ColorPalette(mode)
        self.style = Style(mode)
    }
    
    func switchThemeMode() {
        switch mode {
        case .dark:
            self.mode = .light
            self.colorPalette = ColorPalette(.light)
            self.style = Style(.light)
        case .light:
            self.mode = .dark
            self.colorPalette = ColorPalette(.dark)
            self.style = Style(.dark)
        }
    }
}
