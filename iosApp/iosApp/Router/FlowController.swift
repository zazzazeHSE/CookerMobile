import Foundation

protocol FlowControllerViewProtocol {
    func navigate(to: NavigateTo)
}

class FlowController {
    private var flow = FlowStore()
    var view: FlowControllerViewProtocol?

    private struct FlowStore: Changeable {
        init(copy: ChangeableWrapper<FlowStore>) {
            self.simpleRecipeId = copy.simpleRecipeId
        }

        init() { }

        var simpleRecipeId: String = ""
    }
}

extension FlowController: FlowControllerViewDelegate {
    func makeReceiptsListViewModel() -> ReceiptsViewModel {
        .init { [weak self] id in
            guard let this = self else { return }
            this.flow = this.flow.changing { copy in
                copy.simpleRecipeId = id
            }
            this.view?.navigate(to: .simpleRecipe)
        }
    }

    func makeSimpleRecipeViewModel() -> SimpleRecipeViewModel {
        .init(recipeId: flow.simpleRecipeId)
    }
}

