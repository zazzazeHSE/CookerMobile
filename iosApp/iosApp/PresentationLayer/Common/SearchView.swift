import SwiftUI

struct SearchView: View {
    private let textColor = Color(red: 17, green: 23, blue: 25)
    let title: String
    let text: Binding<String>
    var body: some View {
        HStack {
            HStack {
                Image(systemName: "magnifyingglass")
                Spacer(minLength: 14)
                TextField(title, text: text)
                    .foregroundColor(textColor)
            }
        }
        .padding(18)
        .background(Color(red: 239, green: 239, blue: 239))
        .cornerRadius(10)
    }
}

#if DEBUG
struct SearchView_Previews: PreviewProvider {
    static var previews: some View {
        Group {
            SearchView(
                title: "Введите название блюда...",
                text: .constant("")
            )
                .previewLayout(.sizeThatFits)
                .previewDisplayName("Without input")

            SearchView(
                title: "Введите название блюда...",
                text: .constant("Блины")
            )
                .previewLayout(.sizeThatFits)
                .previewDisplayName("With input")
        }
    }
}
#endif
