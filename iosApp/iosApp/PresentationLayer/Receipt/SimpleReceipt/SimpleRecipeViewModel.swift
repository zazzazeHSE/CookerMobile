import Foundation
import shared

final class SimpleRecipeViewModel: ObservableObject {
    @Published var model: Model<SimpleRecipe> = .loading
    private let presenter = RecipeDetailsStore()

    init(recipeId: String) {
        presenter.attachView(updateCallback: didChangeState(_:))
        presenter.reduce(action: RecipeDetailsAction.Init(id: recipeId))
    }

    private func didChangeState(_ state: RecipeDetailsState?) {
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
