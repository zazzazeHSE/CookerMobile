import SwiftUI
import shared

struct ReceiptsListView: View {
    @ObservedObject var viewModel: ReceiptsViewModel

    var body: some View {
        VStack(alignment: .center) {
            CategoriesList(
                model: $viewModel.categoriesModel,
                onCategoryTap: viewModel.didTapOnCategory(_:)
            )

            ListView(
                model: $viewModel.receiptsModel,
                onRecipeTap: viewModel.didTapToRecipe(_:),
                onScrollToLast: viewModel.didScrollToLastItem,
                onRecipeLikeButtonTap: viewModel.didTapOnLikeForReceipt(_:)
            )
            Spacer()
        }
        .navigationBarTitle(Text("Какой рецепт Вы ищете?"))
    }
}

private struct CategoriesList: View {
    @Binding var model: Model<[CategoryModel]>
    let onCategoryTap: (CategoryModel) -> Void

    var body: some View {
        switch model {
            case .data(let data): categoriesList(data)
            default: EmptyView()
        }
    }

    @ViewBuilder private func categoriesList(_ categories: [CategoryModel]) -> some View {
        ScrollView(.horizontal, showsIndicators: false) {
            HStack {
                ForEach(categories, id: \.id) { category in
                    Text(category.title)
                        .foregroundColor(category.current ? Colors.orange : Colors.darkBlue)
                        .onTapGesture {
                            onCategoryTap(category)
                        }
                }
            }
            .padding(.horizontal, 15)
            .padding(.vertical, 5)
        }
    }
}

private struct ListView: View {
    @Binding var model: Model<ReceiptsListModel>
    let onRecipeTap: (Receipt) -> Void
    let onScrollToLast: () -> Void
    let onRecipeLikeButtonTap: (Receipt) -> Void

    var body: some View {
        switch model {
        case .data(let data): receiptsList(model: data)
        case .error(let error): HStack(alignment: .center) {
            Text(error)
                .foregroundColor(.red)
        }
        case .loading: ProgressView()
        }
    }

    @ViewBuilder private func receiptsList(model: ReceiptsListModel) -> some View {
        List {
            ForEach(model.receipts ?? [], id: \.id) { receipt in
                ReceiptListItemView(
                    receipt: receipt,
                    onLikeButtonTap: { onRecipeLikeButtonTap(receipt) }
                )
                    .padding(.horizontal, 10)
                    .padding(.vertical, 20)
                    .onAppear {
                        if receipt == model.receipts?.last {
                            onScrollToLast()
                        }
                    }
                    .background(.clear)
                    .onTapGesture {
                        onRecipeTap(receipt)
                    }
            }
            .listRowInsets(EdgeInsets())
            .listRowSeparator(.hidden)
            .listRowBackground(Color.white)
            if model.isFull == false {
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
}

#if DEBUG
struct ReceiptsView_Previews: PreviewProvider {
	static var previews: some View {
        ReceiptsListView(
            viewModel: .init(openRecipeView: { _ in })
        )
	}
}
#endif
