import Foundation
import SwiftUI

struct ReceiptListItemView: View {
    let receipt: Receipt
    var body: some View {
        VStack(alignment: .leading) {
            ZStack(alignment: .topTrailing) {
                AsyncImage(
                    url: receipt.imageURL,
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
                .frame(minHeight: 165 ,maxHeight: 165)
                .cornerRadius(18)
                if receipt.favourite {
                    favouriteView
                        .padding(12)
                }
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
        .background(Colors.orange)
        .clipShape(Circle())
    }
}

#if DEBUG
struct ReceiptListItemView_Previews: PreviewProvider {
    static var previews: some View {
        ReceiptListItemView(
            receipt: .init(
                title: "Пряный суп из батата",
                imageURL: URL(string: "https://media.healthkurs.ru/wp-content/uploads/2021/07/sladkij-kartofel.jpg")!,
                rating: 4.5,
                ratesCount: 60,
                favourite: true
            )
        )
            .previewLayout(.device)
    }
}
#endif
