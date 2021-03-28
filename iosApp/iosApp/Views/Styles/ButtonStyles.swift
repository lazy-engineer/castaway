import SwiftUI

public struct NeumorphismCircleButtonStyle: ButtonStyle {
    let themeMode: ThemeMode
   
    public func makeBody(configuration: Self.Configuration) -> some View {
        let theme = ThemeNeumorphism(mode: self.themeMode)
        
        return configuration.label
            .foregroundColor(configuration.isPressed ? .white : theme.colorPalette.primary)
            .padding()
            .contentShape(Circle())
            .background(
                PrimaryHighlightedCircleBackground(theme: theme, isHighlighted: configuration.isPressed, shape: Circle())
            )
            .animation(nil)
    }
}

public struct NeumorphismCircleToggleStyle: ToggleStyle {
    let themeMode: ThemeMode
    
    public func makeBody(configuration: Self.Configuration) -> some View {
        Button(action: {
            configuration.isOn.toggle()
        }) {
            configuration.label
                .padding()
                .contentShape(Circle())
        }
        .background(
            PrimaryHighlightedCircleBackground(theme: ThemeNeumorphism(mode: themeMode), isHighlighted: configuration.isOn, shape: Circle())
        )
    }
}

public struct NeumorphismUnderlineStyle: ButtonStyle {
    let themeMode: ThemeMode
    
    public func makeBody(configuration: Self.Configuration) -> some View {
        let theme = ThemeNeumorphism(mode: self.themeMode)
        
        return configuration.label
            .foregroundColor(theme.colorPalette.primary)
            .padding(8)
            .background(
                UnderlineBackground(theme: theme, isHighlighted: configuration.isPressed, shape: Capsule())
                    .frame(height: 5)
                    .padding(.top, 30)
            )
    }
}

public struct NeumorphismPillStyle: ButtonStyle {
    let themeMode: ThemeMode
    
    public func makeBody(configuration: Self.Configuration) -> some View {
        let theme = ThemeNeumorphism(mode: self.themeMode)
        
        return configuration.label
            .foregroundColor(theme.colorPalette.primary)
            .padding(8)
            .contentShape(Capsule())
            .background(
                NeumorphismPillBackground(theme: theme, isHighlighted: configuration.isPressed, shape: Capsule())
            )
            .animation(nil)
    }
}

struct PrimaryHighlightedCircleBackground<S: Shape>: View {
    var theme: ThemeNeumorphism
    var isHighlighted: Bool
    var shape: S
    
    var body: some View {
        ZStack {
            if isHighlighted {
                shape
                    .fill(LinearGradient(theme.colorPalette.secondary, theme.colorPalette.primary))
                    .overlay(shape.stroke(LinearGradient(theme.colorPalette.primary, theme.colorPalette.secondary), lineWidth: 4))
                    .shadow(color: theme.colorPalette.dropShadow, radius: 10, x: -10, y: -10)
                    .shadow(color: theme.colorPalette.reflection, radius: 10, x: 10, y: 10)
            } else {
                shape
                    .fill(theme.colorPalette.background)
                    .overlay(shape.stroke(LinearGradient(theme.colorPalette.backgroundGradient, theme.colorPalette.background), lineWidth: 4))
                    .overlay(
                        Circle()
                            .stroke(theme.colorPalette.intenseReflection, lineWidth: 4)
                            .blur(radius: 4)
                            .offset(x: 2, y: 2)
                            .mask(Circle().fill(LinearGradient(Color.black, Color.clear)))
                    )
                    .overlay(
                        Circle()
                            .stroke(theme.colorPalette.dropShadow, lineWidth: 8)
                            .blur(radius: 4)
                            .offset(x: -2, y: -2)
                            .mask(Circle().fill(LinearGradient(Color.clear, Color.black)))
                    )
                    .shadow(color: theme.colorPalette.dropShadow, radius: 10, x: 10, y: 10)
                    .shadow(color: theme.colorPalette.reflection, radius: 10, x: -10, y: -10)
            }
        }
    }
}

struct NeumorphismPillBackground<S: Shape>: View {
    var theme: ThemeNeumorphism
    var isHighlighted: Bool
    var shape: S
    
    var body: some View {
        ZStack {
            if isHighlighted {
                shape
                    .fill(LinearGradient(theme.colorPalette.dropShadow, theme.colorPalette.background))
                    .shadow(color: theme.colorPalette.dropShadow, radius: 5, x: -5, y: -5)
                    .shadow(color: theme.colorPalette.reflection, radius: 5, x: 5, y: 5)
            } else {
                shape
                    .fill(theme.colorPalette.background)
                    .overlay(
                        Capsule()
                            .stroke(theme.colorPalette.intenseReflection, lineWidth: 8)
                            .blur(radius: 4)
                            .offset(x: 2, y: 2)
                            .mask(Capsule().fill(LinearGradient(Color.black, Color.clear)))
                    )
                    .overlay(
                        Capsule()
                            .stroke(theme.colorPalette.dropShadow, lineWidth: 8)
                            .blur(radius: 4)
                            .offset(x: -2, y: -2)
                            .mask(Capsule().fill(LinearGradient(Color.clear, Color.black)))
                    )
                    .shadow(color: theme.colorPalette.dropShadow, radius: 5, x: 5, y: 5)
                    .shadow(color: theme.colorPalette.reflection, radius: 5, x: -5, y: -5)
            }
        }
    }
}

struct UnderlineBackground<S: Shape>: View {
    var theme: ThemeNeumorphism
    var isHighlighted: Bool
    var shape: S
    
    var body: some View {
        ZStack {
            if isHighlighted {
                shape
                    .fill(theme.colorPalette.background)
                    .overlay(
                        Capsule()
                            .stroke(theme.colorPalette.dropShadow, lineWidth: 2)
                            .blur(radius: 3)
                            .mask(Capsule().fill(LinearGradient.init(gradient: Gradient(colors: [Color.black, Color.clear]), startPoint: .top, endPoint: .bottom)))
                    )
                    .overlay(
                        Capsule()
                            .stroke(theme.colorPalette.reflection, lineWidth: 3)
                            .offset(y: -1)
                            .blur(radius: 2)
                            .mask(Capsule().fill(LinearGradient.init(gradient: Gradient(colors: [Color.black, Color.clear]), startPoint: .bottom, endPoint: .top)))
                    )
                    .shadow(color: theme.colorPalette.dropShadow, radius: 3, x: -3, y: -3)
                    .shadow(color: theme.colorPalette.reflection, radius: 5, x: 5, y: 5)
            } else {
                shape
                    .fill(theme.colorPalette.background)
                    .overlay(
                        Capsule()
                            .stroke(theme.colorPalette.intenseReflection, lineWidth: 2)
                            .blur(radius: 2)
                            .mask(Capsule().fill(LinearGradient.init(gradient: Gradient(colors: [Color.black, Color.clear]), startPoint: .top, endPoint: .bottom)))
                    )
                    .overlay(
                        Capsule()
                            .stroke(theme.colorPalette.intenseDropShadow, lineWidth: 3)
                            .offset(y: -1)
                            .blur(radius: 1)
                            .mask(Capsule().fill(LinearGradient.init(gradient: Gradient(colors: [Color.black, Color.clear]), startPoint: .bottom, endPoint: .top)))
                    )
                    .shadow(color: theme.colorPalette.dropShadow, radius: 2, x: 2, y: 2)
                    .shadow(color: theme.colorPalette.reflection, radius: 2, x: -2, y: -2)
            }
        }
    }
}
