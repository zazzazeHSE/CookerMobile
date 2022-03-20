import Foundation

protocol FlowControllerViewProtocol {
    func navigate(to: Screen)
    func close(_ screen: Screen)
}

class FlowController {
    private var flow = FlowStore()
    var view: FlowControllerViewProtocol?
    private lazy var timerViewModel: TimerViewModel = {
        return .init(
            openTimerSelectionView: { [weak self] in
                self?.view?.navigate(to: .timeSelectionView)
            },
            closeTimeSelectionView: { [weak self] in
                self?.view?.close(.timeSelectionView)
            }
        )
    }()

    private lazy var receiptsListViewModel: ReceiptsViewModel = {
        .init { [weak self] id in
            guard let this = self else { return }
            this.flow = this.flow.changing { copy in
                copy.simpleRecipeId = id
            }
            this.view?.navigate(to: .simpleRecipe)
        }
    }()

    private lazy var favouritesReceiptsViewModel: FavouritesReceiptsViewModel = {
        .init { [weak self] id in
            guard let this = self else { return }
            this.flow = this.flow.changing { copy in
                copy.simpleRecipeId = id
            }
            this.view?.navigate(to: .simpleRecipe)
        }
    }()

    private struct FlowStore: Changeable {
        init(copy: ChangeableWrapper<FlowStore>) {
            self.simpleRecipeId = copy.simpleRecipeId
        }

        init() { }

        var simpleRecipeId: String = ""
        var simpleRecipeViewModel: SimpleRecipeViewModel?
    }
}

extension FlowController: FlowControllerViewDelegate {
    func makeReceiptsListViewModel() -> ReceiptsViewModel {
        receiptsListViewModel
    }

    func makeSimpleRecipeViewModel() -> SimpleRecipeViewModel {
        let viewModel = SimpleRecipeViewModel(
            recipeId: flow.simpleRecipeId,
            onCloseNoteInputView: { [weak self] in
                self?.view?.close(.noteInputView)
            }
        )
        self.flow.simpleRecipeViewModel = viewModel
        return viewModel
    }

    func makeTimerViewModel() -> TimerViewModel {
        return timerViewModel
    }

    func makeTimeSelectionViewModel() -> TimerViewModel {
        return timerViewModel
    }

    func makeFavouritesReceiptsViewModel() -> FavouritesReceiptsViewModel {
        favouritesReceiptsViewModel
    }

    func makeIngredientsCartViewModel() -> IngredientsCartViewModel {
        .init()
    }

    func makeNoteIconViewModel() -> NoteIconViewModelImpl {
        .init { [weak self] in
            self?.view?.navigate(to: .noteInputView)
        }
    }

    func makeNoteInputViewModel() -> NoteInputViewModelImpl {
        return .init(
            recipeId: self.flow.simpleRecipeId,
            onNoteInputClose: { [weak self] in
                self?.view?.close(.noteInputView)
            }
        )
    }
}

