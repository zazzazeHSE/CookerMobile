import Foundation
import shared

class ReceiptsViewModel: ObservableObject {
    @Published var model = ReceiptsModel(
        listModel: .init(
            receipts: [],
            isFull: false
        ),
        isLoading: true,
        error: nil
    )
    private let presenter = RecipesListStore()
    private let openRecipeView: (String) -> Void

    init(
        openRecipeView: @escaping (String) -> Void
    ) {
        self.openRecipeView = openRecipeView
        presenter.attachView(updateCallback: didChangeState(_:))

        presenter.reduce(action: RecipesListAction.Init())
    }

    func didScrollToLastItem() {
        if model.listModel.isFull { return }

        presenter.reduce(action: RecipesListAction.LoadNextPage())
    }

    func didTapToRecipe(_ recipe: Receipt) {
        openRecipeView(recipe.id)
    }

    private func didChangeState(_ state: RecipesListState?) {
        guard let state = state else {
            return
        }
        var newModel = model
        if let models = state.recipesResource.value as? [Recipe] {
            newModel = newModel.changing { copy in
                copy.listModel.receipts = dtoToReceiptsModel(models)
                copy.error = nil
            }
        } else {
            newModel = newModel.changing { copy in
                copy.error = state.recipesResource.throwable?.message
            }
        }

        model = newModel.changing { copy in
            copy.isLoading = state.isPaginationRecipesLoading
            copy.listModel.isFull = state.isPaginationFull
        }
    }

}

extension ReceiptsViewModel {
    private func dtoToReceiptsModel(_ dto: [Recipe]) -> [Receipt] {
        dto.compactMap { dtoModel -> Receipt? in
            guard let imageURL = URL(string: dtoModel.imageUrl) else { return nil }
            return .init(
                id: dtoModel.id,
                title: dtoModel.title,
                imageURL: imageURL,
                favourite: false
            )
        }
    }
}
