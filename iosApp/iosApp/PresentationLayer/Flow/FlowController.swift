import Foundation

protocol FlowControllerViewProtocol {
    func navigate(to: Screen, from: Screen)
    func close(_ screen: Screen)
}

class FlowController {
    private var flow = FlowStore()
    var view: FlowControllerViewProtocol?
    private lazy var timerViewModel: TimerViewModel = {
        return .init(
            openTimerSelectionView: { [weak self] in
                self?.view?.navigate(to: .timeSelectionView, from: .any)
            },
            closeTimeSelectionView: { [weak self] in
                self?.view?.close(.timeSelectionView)
            }
        )
    }()

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
            this.view?.navigate(to: .simpleRecipe, from: .receiptsList)
        }
    }

    func makeSimpleRecipeViewModel() -> SimpleRecipeViewModel {
        .init(recipeId: flow.simpleRecipeId)
    }

    func makeTimerViewModel() -> TimerViewModel {
        return timerViewModel
    }

    func makeTimeSelectionViewModel() -> TimerViewModel {
        return timerViewModel
    }

    func makeFavouritesReceiptsViewModel() -> FavouritesReceiptsViewModel {
        .init { [weak self] id in
            guard let this = self else { return }
            this.flow = this.flow.changing { copy in
                copy.simpleRecipeId = id
            }
            this.view?.navigate(to: .simpleRecipe, from: .favouritesList)
        }
    }

    func makeIngredientsCartViewModel() -> IngredientsCartViewModel {
        .init()
    }
}

