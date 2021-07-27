import SwiftUI
import shared

extension CommonColor {
    func toColor() -> Color {
        return Color(red: Double(red) / 255, green: Double(green) / 255, blue: Double(blue) / 255).opacity(alpha)
    }
}
