import SwiftUI

public struct ColorfulButtonStyle: ButtonStyle {
    public func makeBody(configuration: Self.Configuration) -> some View {
        configuration.label
            .padding()
            .contentShape(Circle())
            .background(
                ColorfulBackground(isHighlighted: configuration.isPressed, shape: Circle())
            )
            .animation(nil)
    }
}

public struct ColorfulToggleStyle: ToggleStyle {
    public func makeBody(configuration: Self.Configuration) -> some View {
        Button(action: {
            configuration.isOn.toggle()
        }) {
            configuration.label
                .padding()
                .contentShape(Circle())
        }
        .background(
            ColorfulBackground(isHighlighted: configuration.isOn, shape: Circle())
        )
    }
}

struct RectangleButtonStyle: ButtonStyle {
    
    func makeBody(configuration: Self.Configuration) -> some View {
        configuration.label
            .padding(30)
            .contentShape(RoundedRectangle(cornerRadius: 25.0))
            .background(
                RoundedRectangle(cornerRadius: 25.0)
                    .fill(Color.lightThemeBackground)
                    .shadow(color: Color.lightThemeDarkShadow, radius: /*@START_MENU_TOKEN@*/10/*@END_MENU_TOKEN@*/, x: 10, y: 10)
                    .shadow(color: Color.lightThemeLightShadow, radius: /*@START_MENU_TOKEN@*/10/*@END_MENU_TOKEN@*/, x: -5, y: -5)
            )
    }
}

struct RoundButtonStyle: ButtonStyle {
    
    func makeBody(configuration: Self.Configuration) -> some View {
        configuration.label
            .padding(30)
            .contentShape(Circle())
            .background(
                Circle()
                    .fill(Color.lightThemeBackground)
                    .shadow(color: Color.lightThemeDarkShadow, radius: /*@START_MENU_TOKEN@*/10/*@END_MENU_TOKEN@*/, x: 10, y: 10)
                    .shadow(color: Color.lightThemeLightShadow, radius: /*@START_MENU_TOKEN@*/10/*@END_MENU_TOKEN@*/, x: -5, y: -5)
            )
    }
}

struct SimpleButtonStyle: ButtonStyle {
    
    func makeBody(configuration: Self.Configuration) -> some View {
        configuration.label
            .padding(30)
            .contentShape(Circle())
            .background(
                Group {
                    if configuration.isPressed {
                        Circle()
                            .fill(Color.lightThemeBackground)
                            .overlay(
                                Circle()
                                    .stroke(Color.white, lineWidth: 4)
                                    .blur(radius: 4)
                                    .offset(x: 2, y: 2)
                                    .mask(Circle().fill(LinearGradient(Color.black, Color.clear)))
                            )
                            .overlay(
                                Circle()
                                    .stroke(Color.lightThemeDarkShadow, lineWidth: 8)
                                    .blur(radius: 4)
                                    .offset(x: -2, y: -2)
                                    .mask(Circle().fill(LinearGradient(Color.clear, Color.black)))
                            )
                    } else {
                        Circle()
                            .fill(Color.lightThemeBackground)
                            .shadow(color: Color.lightThemeDarkShadow, radius: 10, x: 10, y: 10)
                            .shadow(color: Color.lightThemeLightShadow, radius: 10, x: -5, y: -5)
                    }
                }
            )
    }
}

struct ColorfulBackground<S: Shape>: View {
    var isHighlighted: Bool
    var shape: S
    
    var body: some View {
        ZStack {
            if isHighlighted {
                shape
                    .fill(LinearGradient(Color.blueGradientEnd, Color.blueGradientStart))
                    .overlay(shape.stroke(LinearGradient(Color.blueGradientStart, Color.blueGradientEnd), lineWidth: 4))
                    .shadow(color: Color.lightThemeDarkShadow, radius: 10, x: -10, y: -10)
                    .shadow(color: Color.lightThemeLightShadow, radius: 10, x: 10, y: 10)
            } else {
                shape
                    .fill(Color.lightThemeBackground)
                    .overlay(shape.stroke(LinearGradient(Color.gray.opacity(0.2), Color.lightThemeBackground), lineWidth: 4))
                    .overlay(
                        Circle()
                            .stroke(Color.white, lineWidth: 4)
                            .blur(radius: 4)
                            .offset(x: 2, y: 2)
                            .mask(Circle().fill(LinearGradient(Color.black, Color.clear)))
                    )
                    .overlay(
                        Circle()
                            .stroke(Color.lightThemeDarkShadow, lineWidth: 8)
                            .blur(radius: 4)
                            .offset(x: -2, y: -2)
                            .mask(Circle().fill(LinearGradient(Color.clear, Color.black)))
                    )
                    .shadow(color: Color.lightThemeDarkShadow, radius: 10, x: 10, y: 10)
                    .shadow(color: Color.lightThemeLightShadow, radius: 10, x: -10, y: -10)
            }
        }
    }
}
