import SwiftUI
import shared

protocol ReceiptsViewRouter: Router {

}

struct ReceiptsView: View {
    let router: ReceiptsViewRouter
    @State private var selectedMenuState: MenuItems = .popular
    let receipts: [Receipt] = {
        var arr = [Receipt]()
        for _ in 0...10 {
            arr.append(
                Receipt(
                    title: "Пряный суп из батата",
                    imageURL: URL(string: "https://media.healthkurs.ru/wp-content/uploads/2021/07/sladkij-kartofel.jpg")!,
                    rating: 4.5,
                    ratesCount: 60,
                    favourite: .random()
                )
            )
        }

        return arr
    }()

	var body: some View {
        NavigationView {
            ScrollView {
            VStack(alignment: .leading) {
                titleText
                searchInput
                filterView
                receiptsList
                Spacer()
            }
            .padding()
            .navigationBarHidden(true)
            }
        }
	}

    private var titleText: some View {
        Text("Какой рецепт Вы ищете?")
            .font(.system(size: 30))
            .bold()
            .foregroundColor(Colors.darkBlue)
    }

    private var searchInput: some View {
        SearchView(
            title: "Введите название рецепта...",
            text: .constant("")
        )
            .background(Color(red: 252, green: 252, blue: 253))
            .cornerRadius(15)
            .overlay(
                RoundedRectangle(cornerRadius: 10)
                    .stroke(
                        Color(red: 239, green: 239, blue: 239),
                        lineWidth: 1
                    )
            )
            .font(.system(size: 14))
    }

    private var filterView: some View {
        HStack {
            Text("Сортировать по:")
            Menu(selectedMenuState.rawValue) {
                ForEach(MenuItems.allCases, id: \.self) { item in
                    Button(item.rawValue) {
                        selectedMenuState = item
                    }
                }
            }
            .foregroundColor(Colors.orange)
        }
        .font(.system(size: 14))
    }

    private var receiptsList: some View {
        ForEach(receipts, id: \.id) { receipt in
            ReceiptListItemView(receipt: receipt)
                .padding(.bottom, 20)
        }
    }

    private enum MenuItems: String, CaseIterable, CustomStringConvertible {
        var description: String {
            self.rawValue
        }

        case popular = "Популярность"
        case rating = "Рейтинг"
    }
}

#if DEBUG
struct ReceiptsView_Previews: PreviewProvider {
	static var previews: some View {
        ReceiptsView(router: ReceiptsRouter(isPresented: .constant(false)))
	}
}
#endif
