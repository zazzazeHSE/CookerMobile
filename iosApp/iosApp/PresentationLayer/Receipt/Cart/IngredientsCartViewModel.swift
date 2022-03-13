import Foundation
import SwiftUI
import shared

final class IngredientsCartViewModel: BaseViewModel<IngredientsCartState, IngredientsCartEffect> {
    @Published var ingredientsModel: Model<[Ingredient]> = .loading
    private lazy var store: IngredientsCartStore = {
        let store = IngredientsCartStore()
        store.observeState().collect(collector: collector, completionHandler: {_, _ in})
        return store
    }()

    override init() {
        super.init()
        store.reduce(action: IngredientsCartAction.Init())
    }

    override func didChangeState(_ state: IngredientsCartState?) {
        guard let resource = state?.ingredientsCartResource else { return }
        if let value = resource.value as? [Ingredient] {
            ingredientsModel = .data(value)
        } else if let error = resource.throwable {
            ingredientsModel = .error(error.message ?? "Unexpected Error")
        } else {
            ingredientsModel = .loading
        }
    }
}

extension IngredientsCartViewModel: IngredientsListViewModel {
    var model: [Ingredient] {
        get {
            switch ingredientsModel {
            case .data(let data): return data
            default: return []
            }
        }
    }

    func didTapOnSelectIngredient(_ ingredient: Ingredient) {
        store.reduce(action: IngredientsCartAction.Select(ingredient: ingredient))
    }


}
