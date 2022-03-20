import Foundation
import shared

final class SearchListViewModelImpl: BaseViewModel<SearchRecipesState, SearchRecipesEffect>, SearchListViewModel {
    @Published var model: Model<ReceiptsListModel> = .loading

    @Published var searchText: String = "" {
        didSet {
            store.reduce(action: SearchRecipesAction.SetSearchQuery(searchQuery: searchText))
        }
    }

    private let store : SearchRecipesStore
    private let openRecipeView: (String) -> Void

    init(openRecipeView: @escaping (String) -> Void) {
        store = SearchRecipesStore()
        self.openRecipeView = openRecipeView
        super.init()
        store.stateFlow.collect(collector: collector, completionHandler: {_, _ in})
        store.reduce(action: SearchRecipesAction.Init())
    }

    override func didChangeState(_ state: SearchRecipesState?) {
        guard let state = state else {
            return
        }

        if
            state.recipesResource.isData,
            let recipesList = state.recipesResource.value as? [Recipe]
        {
            let models = dtoToReceiptsModel(recipesList)
            let isFull = state.isPaginationFull
            model = .data(
                .init(
                    receipts: models,
                    isPaginationLoading: state.isPaginationLoading,
                    isFull: isFull
                )
            )
        } else if state.isPaginationError {
            model = .error("Ошибка загрузки рецептов")
        } else if let error = state.recipesResource.throwable {
            model = .error(error.message ?? "Unknown Error")
        } else {
            model = .loading
        }
    }

    func onRecipeTap(_ receipt: Receipt) {
        openRecipeView(receipt.id)
    }

    func onScrollToLast() {
        store.reduce(action: SearchRecipesAction.LoadMore())
    }

    func onRecipeLikeButtonTap(_ receipt: Receipt) {
        store.reduce(action: SearchRecipesAction.Like(recipeId: receipt.id))
    }
}

extension SearchListViewModelImpl {
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
}
