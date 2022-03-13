import Foundation
import shared

final class SimpleRecipeViewModel: BaseViewModel<RecipeDetailsState> {
    @Published var model: Model<SimpleRecipe> = .loading
    private lazy var store: RecipeDetailsStore = {
        let store = RecipeDetailsStore()
        store.observeState().collect(collector: collector, completionHandler: {_,_ in })
        return store
    }()

    init(recipeId: String) {
        super.init()
        store.reduce(action: RecipeDetailsAction.Init(id: recipeId))
    }

    override func didChangeState(_ state: RecipeDetailsState?) {
        guard let state = state else {
            return
        }

        if let value = state.recipeResource.value {
            model = .data(dtoToModel(dto: value))
        } else if let error = state.recipeResource.throwable {
            model = .error(error.message ?? "Unexpected error")
        } else {
            model = .loading
        }
    }

    func didTapOnLikeForReceipt(_ recipe: SimpleRecipe) {
        store.reduce(action: RecipeDetailsAction.Like(id: recipe.id))
    }
}

extension SimpleRecipeViewModel {
    func dtoToModel(dto: RecipeDetails) -> SimpleRecipe {
        return .init(
            id: dto.id,
            title: dto.title,
            description: dto.description_,
            favourite: dto.isLiked,
            imageUrl: URL(string: dto.imageUrl),
            ingredients: dto.ingredients.map { .init(name: $0.name, value: $0.value) },
            steps: dto.steps.map { .init(imageUrl: URL(string: $0.imageUrl), steps: $0.steps) }
        )
    }
}
