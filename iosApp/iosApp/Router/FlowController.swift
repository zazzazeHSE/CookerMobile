import Foundation

protocol FlowControllerViewProtocol {
    func navigate(to: Navigate)
    func close(_ screen: Navigate)
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

    func makeTimerViewModel() -> TimerViewModel {
        .init { [weak self] in
            guard let this = self else { return }
            this.view?.navigate(to: .timeSelectionView)
        }
    }

    func makeTimeSelectionViewModel() -> TimeSelectionViewModel {
        .init { [weak self] in
            self?.view?.close(.timeSelectionView)
        }
    }
}

