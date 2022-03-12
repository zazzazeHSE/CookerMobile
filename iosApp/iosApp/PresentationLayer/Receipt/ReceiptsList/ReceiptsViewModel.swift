import Foundation
import shared

class ReceiptsViewModel: ObservableObject {
    @Published var receiptsModel: Model<ReceiptsListModel> = .loading
    @Published var categoriesModel: Model<[CategoryModel]> = .loading
    private lazy var store: RecipesListStore = {
        let store = RecipesListStore()
        store.observeState().collect(collector: collector, completionHandler: {_, _ in})
        return store
    }()

    private lazy var collector: Observer = {
        let collector = Observer { [weak self] value in
            if let value = value as? RecipesListState? {
                self?.didChangeState(value)
            }
        }
        return collector
    }()

    private let openRecipeView: (String) -> Void

    init(
        openRecipeView: @escaping (String) -> Void
    ) {
        self.openRecipeView = openRecipeView

        store.reduce(action: RecipesListAction.Init())
    }

    func didScrollToLastItem() {
        switch receiptsModel {
        case .data(let data):
            if data.isFull { return }
        default: break
        }

        store.reduce(action: RecipesListAction.LoadNextPage())
    }

    func didTapToRecipe(_ recipe: Receipt) {
        openRecipeView(recipe.id)
    }

    func didTapOnCategory(_ category: CategoryModel) {
        store.reduce(
            action: RecipesListAction.ChangeCategory(
                category: .init(
                    id: category.id,
                    name: category.title
                )
            )
        )
    }

    func didTapOnLikeForReceipt(_ recipe: Receipt) {
        store.reduce(action: RecipesListAction.Like(id: recipe.id))
    }

    private func didChangeState(_ state: RecipesListState?) {
        guard let state = state else {
            return
        }

        updateReceiptsModel(state)
        updateCategories(state)
    }

    private func updateReceiptsModel(_ state: RecipesListState) {
        if let models = state.recipesResource.value as? [Recipe] {
            let newModels = dtoToReceiptsModel(models)
            let isFull = state.isPaginationFull
            receiptsModel = .data(.init(receipts: newModels, isFull: isFull))
        } else if let error = state.recipesResource.throwable {
            receiptsModel = .error(error.message ?? "Unexpected error")
        } else {
            receiptsModel = .loading
        }
    }

    private func updateCategories(_ state: RecipesListState) {
        if let models = state.categoriesResource.value as? [shared.Category] {
            let newModels = dtoToCategoriesModel(models)
            let modelsWithCurrent = newModels.map { (obj: CategoryModel) -> CategoryModel in
                if
                    let id = state.category?.id,
                    id == obj.id {
                    return obj.changing { copy in
                        copy.current = true
                    }
                }
                return obj
            }
            categoriesModel = .data(modelsWithCurrent)
        } else if let error = state.categoriesResource.throwable {
            categoriesModel = .error(error.message ?? "Unexpected error")
        } else {
            categoriesModel = .loading
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
                favourite: dtoModel.isLiked
            )
        }
    }

    private func dtoToCategoriesModel(_ dto: [shared.Category]) -> [CategoryModel] {
        dto.map {
            .init(id: $0.id, title: $0.name, current: false)
        }
    }
}
