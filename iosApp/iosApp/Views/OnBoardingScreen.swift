import SwiftUI

struct OnBoardingScreen: View {
    
    @EnvironmentObject var theme: ThemeNeumorphism
    
    @State var isDarkTheme: Bool = false
    
    let onFinished: (Bool) -> Void
    
    var body: some View {
        VStack {
            Spacer()
            
            Text("Choose your Style")
                .font(.title2)
                .bold()
                .foregroundColor(theme.colorPalette.textColor)
            
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
                    .toggleStyle(SwitchToggleStyle(tint: theme.colorPalette.primary))
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
            
            Spacer()
            
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
