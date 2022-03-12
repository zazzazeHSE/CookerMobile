import Foundation
import shared

final class SimpleReceiptViewModel {
    @Published var model: Model<SimpleRecipe> = .loading
    private let presenter = DetailedRecipeStore()

    init(recipeId: String) {
        presenter.attachView(updateCallback: didChangeState(_:))
        presenter.reduce(action: DetailedRecipeAction.Init(id: recipeId))
    }

    private func didChangeState(_ state: DetailedRecipeState?) {
        guard let state = state else {
            return
        }

        if let value = state.recipeResource.value {
            model = .data(dtoToModel(dto: value))
        } else if let error = state.recipeResource.throwable {
            model = .error(error.message ?? "Unexpected error")
        } else {
            model = .error("Unexpected error")
        }
    }

}

extension SimpleReceiptViewModel {
    func dtoToModel(dto: DetailedRecipe) -> SimpleRecipe {
        return .init(
            id: dto.id,
            title: dto.title,
            description: dto.description_,
            favourite: false,
            imageUrl: URL(string: dto.imageUrl),
            ingredients: dto.ingredients.map { .init(name: $0.name, value: $0.value) },
            steps: dto.steps.map { .init(imageUrl: URL(string: $0.imageUrl), steps: $0.steps) }
        )
    }
}
