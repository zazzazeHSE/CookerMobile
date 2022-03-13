import Foundation
import SwiftUI
import shared

final class FavouritesReceiptsViewModel: BaseViewModel<FavouritesState, FavouritesEffect> {
    @Published var model: Model<ReceiptsListModel> = .loading
    private lazy var store: FavouritesStore = {
        let store = FavouritesStore()
        store.observeState().collect(collector: collector, completionHandler: {_, _ in})
        return store
    }()

    private let openRecipeView: (String) -> Void

    init(
        openRecipeView: @escaping (String) -> Void
    ) {
        self.openRecipeView = openRecipeView
        super.init()
        store.reduce(action: FavouritesAction.Init())
    }

    override func didChangeState(_ state: FavouritesState?) {
        if let models = state?.recipesResource.value as? [Recipe] {
            let newModels = dtoToReceiptsModel(models)
            model = .data(.init(receipts: newModels, isFull: true))
        } else if let error = state?.recipesResource.throwable {
            model = .error(error.message ?? "Unexpected error")
        } else {
            model = .loading
        }
    }
}

extension FavouritesReceiptsViewModel: ReceiptsListViewModel {
    func onRecipeTap(_ receipt: Receipt) {
        openRecipeView(receipt.id)
    }

    func onScrollToLast() { }

    func onRecipeLikeButtonTap(_ receipt: Receipt) {
        store.reduce(action: FavouritesAction.Like(id: receipt.id))
    }
}

extension FavouritesReceiptsViewModel {
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
