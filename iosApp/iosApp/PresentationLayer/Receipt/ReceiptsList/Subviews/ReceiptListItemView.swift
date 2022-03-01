import Foundation
import SwiftUI

struct ReceiptListItemView: View {
    let receipt: Receipt
    var body: some View {
        VStack(alignment: .leading) {
            ReceiptImageView(
                model: .init(
                    imageURL: receipt.imageURL,
                    isLiked: receipt.favourite
                )
                .frame(minHeight: 165 ,maxHeight: 165)
                .cornerRadius(18)
                favouriteView
                    .padding(12)
            }
            Text(receipt.title)
                .padding(.top, 14)
                .padding(.bottom, 16)
                .padding(.horizontal, 13)
                .font(.system(size: 18, weight: .semibold))
        }
        .background(Color.white)
        .clipped()
        .cornerRadius(18)
        .shadow(
            color: Color(red: 211, green: 209, blue: 216, alpha: 0.75),
            radius: 36,
            x: 18,
            y: 18
        )
    }

    private var favouriteView: some View {
        VStack (alignment: .center) {
            Images.heart
                .foregroundColor(.white)
                .padding(9)
        }
        .background(receipt.favourite ? Colors.orange: .gray )
        .clipShape(Circle())
    }

#if DEBUG
struct ReceiptListItemView_Previews: PreviewProvider {
    static var previews: some View {
        ReceiptListItemView(
            receipt: .init(
                id: "1341",
                title: "Пряный суп из батата",
                imageURL: URL(string: "https://media.healthkurs.ru/wp-content/uploads/2021/07/sladkij-kartofel.jpg")!,
                favourite: true
            )
        )
            .previewLayout(.sizeThatFits)
    }
}
#endif
