import SwiftUI
import shared

struct CategoriesReceiptsListView: View {
    @ObservedObject var viewModel: ReceiptsViewModel

    var body: some View {
        VStack(alignment: .center) {
            CategoriesList(
                model: $viewModel.categoriesModel,
                onCategoryTap: viewModel.didTapOnCategory(_:)
            )

            ReceiptsListView(
                viewModel: viewModel
            )
            Spacer()
        }
        .navigationTitle("Какой рецепт Вы ищете?")
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

#if DEBUG
struct ReceiptsView_Previews: PreviewProvider {
	static var previews: some View {
        CategoriesReceiptsListView(
            viewModel: .init(openRecipeView: { _ in })
        )
	}
}
#endif
