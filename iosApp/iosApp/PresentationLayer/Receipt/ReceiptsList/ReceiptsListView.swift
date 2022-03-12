import SwiftUI
import shared

protocol ReceiptsViewRouter: Router {

}

struct ReceiptsListView: View {
    let router: ReceiptsViewRouter
    @State private var selectedMenuState: MenuItems = .popular
    @ObservedObject var viewModel: ReceiptsViewModel

    init(
        router: ReceiptsViewRouter,
        viewModel: ReceiptsViewModel
    ) {
        self.router = router
        self.viewModel = viewModel
    }

	var body: some View {
        NavigationView {
            VStack {
                receiptsList
            }
            .navigationBarTitle(Text("Какой рецепт Вы ищете?"))
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
        List {
            ForEach(viewModel.model.listModel.receipts ?? [], id: \.id) { receipt in
                ReceiptListItemView(receipt: receipt)
                    .padding(.horizontal, 10)
                    .padding(.vertical, 20)
                    .onAppear {
                        if receipt == viewModel.model.listModel.receipts?.last {
                            viewModel.didScrollToLastItem()
                        }
                    }
                    .background(.clear)
            }
            .listRowInsets(EdgeInsets())
            .listRowSeparator(.hidden)
            .listRowBackground(Color.white)
            if viewModel.model.listModel.isFull == false {
                HStack(alignment: .center) {
                    Spacer()
                    ProgressView()
                    Spacer()
                }
                .listRowInsets(EdgeInsets())
                .listRowSeparator(.hidden)
                .listRowBackground(Color.white)
            }
        }
        .listStyle(PlainListStyle())
        .background(.white)
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
        ReceiptsListView(
            router: ReceiptsRouter(
                isPresented: .constant(false)
            ),
            viewModel: .init()
        )
	}
}
#endif
