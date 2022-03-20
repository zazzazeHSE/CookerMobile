import Foundation
import shared

final class NoteIconViewModelImpl: NoteIconViewModel {
    private let openNoteInputView: () -> Void

    init(openNoteInputView: @escaping () -> Void) {
        self.openNoteInputView = openNoteInputView
    }

    func didTapOnIcon() {
        openNoteInputView()
    }
}

final class NoteInputViewModelImpl: BaseViewModel<RecipeNoteState, RecipeNoteEffect>, NoteInputViewModel  {
    private let store: RecipeNoteStore

    @Published var inputText: String = "" {
        didSet {
            store.reduce(action: RecipeNoteAction.SetContent(content: inputText))
        }
    }
    private let onNoteInputClose: () -> Void

    init(recipeId: String, onNoteInputClose: @escaping () -> Void) {
        self.store = RecipeNoteStore(recipeId: recipeId)
        self.onNoteInputClose = onNoteInputClose
        super.init()
        store.stateFlow.collect(collector: collector, completionHandler: { _, _ in })
        store.reduce(action: RecipeNoteAction.Init())
    }

    func didTapClose() {
        onNoteInputClose()
    }

    override func didChangeState(_ state: RecipeNoteState?) {
        guard let state = state else {
            return
        }

        self.inputText = state.content
    }
}
