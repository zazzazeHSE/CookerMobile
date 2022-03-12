import SwiftUI

struct ReceiptImageModel {
    let imageURL: URL?
    let isLiked: Bool
}

struct ReceiptImageView: View {
    private enum ViewColors {
        static let grey = Color(red: 224, green: 224, blue: 224)
    }
    let model: ReceiptImageModel
    var body: some View {
        ZStack(alignment: .topTrailing) {
            AsyncImage(
                url: model.imageURL,
                scale: 1.0,
                content: { image in
                    image
                        .resizable()
                        .aspectRatio(contentMode: .fill)
                },
                placeholder: {
                    ProgressView()
                }
            )
                .frame(minWidth: 100, minHeight: 165, maxHeight: 165)
            .cornerRadius(18)
            favouriteView
                .padding(12)
        }
    }

    private var favouriteView: some View {
        VStack (alignment: .center) {
            Images.heart
                .foregroundColor(.white)
                .padding(9)
        }
        .background((model.isLiked ? Colors.orange : ViewColors.grey))
        .clipShape(Circle())
    }
}

#if DEBUG
struct ReceiptImageView_Previews: PreviewProvider {
    static var previews: some View {
        ReceiptImageView(
            model: .init(
                imageURL: URL(string: "https://media.healthkurs.ru/wp-content/uploads/2021/07/sladkij-kartofel.jpg")!,
                isLiked: true
            )
        )
            .previewLayout(.sizeThatFits)
            .previewDisplayName("Liked")

        ReceiptImageView(
            model: .init(
                imageURL: URL(string: "https://media.healthkurs.ru/wp-content/uploads/2021/07/sladkij-kartofel.jpg")!,
                isLiked: true
            )
        )
            .previewLayout(.sizeThatFits)
            .previewDisplayName("Not liked")
    }
}
#endif
