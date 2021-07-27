import SwiftUI
import shared

public class ThemeNeumorphism: ObservableObject {
    
    @Published public var mode: ThemeMode
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
                self.primary = Colors.init().orangeGradientStart.toColor()
                self.secondary = Colors.init().orangeGradientEnd.toColor()
                self.primaryVariant = Colors.init().orangeGradientMiddle.toColor()
                self.textColor = Colors.init().darkThemeTextColor.toColor()
                self.background = Colors.init().darkThemeBackground.toColor()
                self.backgroundGradient = Colors.init().darkThemeDarkShadow.toColor()
                self.dropShadow = Colors.init().darkThemeDarkShadow.toColor()
                self.reflection = Colors.init().darkThemeLightShadow.toColor()
                self.intenseDropShadow = Colors.init().darkThemeIntenseDropShadow.toColor()
                self.intenseReflection = Colors.init().darkGray.toColor()
                
            case .light:
                self.primary = Colors.init().blueGradientStart.toColor()
                self.secondary = Colors.init().blueGradientEnd.toColor()
                self.primaryVariant = Colors.init().blueGradientMiddle.toColor()
                self.textColor = Colors.init().lightThemeTextColor.toColor()
                self.background = Colors.init().lightThemeBackground.toColor()
                self.backgroundGradient = Colors.init().lightThemeBackgroundGradient.toColor()
                self.dropShadow = Colors.init().lightThemeDarkShadow.toColor()
                self.reflection = Colors.init().lightThemeLightShadow.toColor()
                self.intenseDropShadow = Colors.init().darkThemeDarkShadow.toColor()
                self.intenseReflection =  Colors.init().white.toColor()
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
    
    public init(mode: ThemeMode = .dark) {
        self.mode = mode
        self.colorPalette = ColorPalette(mode)
        self.style = Style(mode)
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
