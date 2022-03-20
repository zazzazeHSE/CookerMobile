import SwiftUI
import shared

protocol SearchListViewModel: ReceiptsListViewModel  {
    var searchText: String { get set }
}

struct SearchListView<ViewModel>: View where ViewModel: SearchListViewModel {
    @ObservedObject var viewModel: ViewModel
    var body: some View {
        VStack {
            SearchView(
                title: "Введите название рецепта...",
                text: $viewModel.searchText
            )
                .padding(.horizontal, 20)
            if viewModel.searchText.isEmpty {
                searchMessage
                    .padding(.top, 20)
            } else {
                ReceiptsListView(viewModel: viewModel)
            }
            Spacer()
        }
        .navigationTitle("Поиск")
    }

    private var searchMessage: some View {
        VStack {
            Image(systemName: "magnifyingglass")
                .resizable()
                .frame(width: 70, height: 70)
            Text("Начните вводить название рецепта")
        }
    }
}

#if DEBUG
struct SearchListView_Previews: PreviewProvider {
    static var previews: some View {
        SearchListView(viewModel: SearchListViewModel_Preview())
    }
}

private final class SearchListViewModel_Preview: SearchListViewModel {
    var model: Model<ReceiptsListModel> = .loading
    var searchText: String = ""

    func onRecipeTap(_ receipt: Receipt) {

    }

    func onScrollToLast() {

    }

    func onRecipeLikeButtonTap(_ receipt: Receipt) {

    }
}
#endif
