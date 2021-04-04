import SwiftUI

struct OnBoardingScreen: View {
    
    @EnvironmentObject var theme: ThemeNeumorphism
    
    @State var isDarkTheme: Bool = false
    @State var imageOffset: CGSize = .zero
    
    let onFinished: (Bool) -> Void
    
    var body: some View {
        VStack {
            
            if isDarkTheme {
                Image(systemName: "moon.fill")
                    .resizable()
                    .scaledToFit()
                    .frame(width: 100, height: 100)
                    .selfSizeMask(
                        LinearGradient(
                            gradient: Gradient(colors: [Color.blueGradientStart, Color.blueGradientEnd]),
                            startPoint: .top,
                            endPoint: .bottom)
                    )
                    .shadow(color: theme.colorPalette.dropShadow, radius: 10, x: 10, y: 10)
                    .padding(.top, 80)
                    .padding(48)
                    .transition(.move(edge: .top))
                    .animation(.easeOut(duration: 1))
            } else {
                Circle()
                    .scaledToFit()
                    .frame(width: 100, height: 100)
                    .selfSizeMask(
                        LinearGradient(
                            gradient: Gradient(colors: [Color.yellow, Color.red]),
                            startPoint: .topTrailing,
                            endPoint: .bottom)
                    )
                    .shadow(color: theme.colorPalette.dropShadow, radius: 10, x: 10, y: 10)
                    .padding(.top, 80)
                    .padding(48)
                    .transition(.move(edge: .top))
                    .animation(.easeOut(duration: 1))
            }
            
            Text("Choose Your Destiny")
                .font(.title2)
                .bold()
                .foregroundColor(theme.colorPalette.textColor)
            
            if isDarkTheme {
                VStack(spacing: 0) {
                    Text("\"The night is darkest just before the dawn. And I promise you, the dawn is coming.\"")
                        .font(.headline)
                        .bold()
                        .foregroundColor(theme.colorPalette.textColor)
                        .multilineTextAlignment(.center)
                    
                    Text("- Harvey Dent")
                        .font(.headline)
                        .italic()
                        .foregroundColor(theme.colorPalette.textColor)
                }
                .padding()
            } else {
                Text("\"- Hamid: What's that? \n - Rambo: It's blue light. \n - Hamid: What does it do? \n - Rambo: It turns blue.\"")
                    .font(.headline)
                    .bold()
                    .foregroundColor(theme.colorPalette.textColor)
                    .multilineTextAlignment(.center)
                    .padding()
            }
            
            Spacer()
            
            HStack {
                Spacer()
                
                Image(systemName: "sun.min.fill")
                    .resizable()
                    .scaledToFill()
                    .foregroundColor(theme.colorPalette.primary)
                    .frame(width: 24, height: 24)
                
                Toggle("Theme Toggle", isOn: $isDarkTheme)
                    .onChange(of: isDarkTheme, perform: { value in
                        withAnimation {
                            if isDarkTheme {
                                theme.changeThemeMode(mode: .dark)
                            } else {
                                theme.changeThemeMode(mode: .light)
                            }
                        }
                    })
                    .toggleStyle(theme.style.textToggleStyle)
                    .labelsHidden()
                    .padding(16)
                    .onAppear {
                        switch theme.mode {
                        case .dark:
                            isDarkTheme = true
                        case .light:
                            isDarkTheme = false
                        }
                    }
                
                Image(systemName: "moon.stars.fill")
                    .resizable()
                    .scaledToFill()
                    .foregroundColor(theme.colorPalette.primary)
                    .frame(width: 24, height: 24)
                
                Spacer()
            }
            .padding(.bottom, 48)
            
            Button(action: {
                onFinished(true)
            }) {
                Image(systemName: "arrow.right")
                    .frame(width: 30, height: 30)
            }
            .buttonStyle(theme.style.roundButtonStyle)
            .padding(.bottom, 48)
        }
        .background(theme.colorPalette.background.edgesIgnoringSafeArea(.all))
    }
}

#if DEBUG
struct OnBoardingScreen_Previews: PreviewProvider {
    static var previews: some View {
        OnBoardingScreen() { _ in }.environmentObject(ThemeNeumorphism())
    }
}
#endif
