import Foundation
import shared

final class SimpleRecipeViewModel: BaseViewModel<RecipeDetailsState, RecipeDetailsEffect> {
    @Published var recipeModel: Model<SimpleRecipe> = .loading
    @Published var inputText: String
    private let store: RecipeDetailsStore
    private let onCloseNoteInputView: () -> Void

    init(
        recipeId: String,
        onCloseNoteInputView: @escaping () -> Void
    ) {
        self.store = RecipeDetailsStore(recipeId: recipeId)
        self.onCloseNoteInputView = onCloseNoteInputView
        self.inputText = ""
        super.init()
        store.observeState().collect(collector: collector, completionHandler: {_,_ in })
        store.reduce(action: RecipeDetailsAction.Init())
    }

    override func didChangeState(_ state: RecipeDetailsState?) {
        guard let state = state else {
            return
        }

        if let value = state.recipeResource.value {
            recipeModel = .data(dtoToModel(dto: value))
        } else if let error = state.recipeResource.throwable {
            recipeModel = .error(error.message ?? "Unexpected error")
        } else {
            recipeModel = .loading
        }
    }

    func didTapOnLikeForReceipt(_ recipe: SimpleRecipe) {
        store.reduce(action: RecipeDetailsAction.Like())
    }
}

extension SimpleRecipeViewModel: IngredientsListViewModel {
    var model: [Ingredient] {
        get {
            switch recipeModel {
            case .data(let data): return data.ingredients
            default: return []
            }
        }
    }

    func didTapOnSelectIngredient(_ ingredient: Ingredient) {
        store.reduce(
            action: RecipeDetailsAction.SelectIngredient(
                ingredient: ingredient
            )
        )
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
            ingredients: dto.ingredients,
            steps: dto.steps.map { .init(imageUrl: URL(string: $0.imageUrl), steps: $0.steps) }
        )
    }
}
