import Foundation

import SwiftUI

protocol FlowControllerViewDelegate: AnyObject {
    func makeReceiptsListViewModel() -> ReceiptsViewModel
    func makeSimpleRecipeViewModel() -> SimpleRecipeViewModel
}

enum NavigateTo {
    case receiptsList
    case simpleRecipe
}

struct FlowControllerView: View, FlowControllerViewProtocol {
    weak var delegate: FlowControllerViewDelegate!

    private let navigateToSimpleRecipeScreen = FlowState()

    init(modelDelegate: FlowControllerViewDelegate) {
        self.delegate = modelDelegate
    }

    func navigate(to navigateTo: NavigateTo) {
        switch navigateTo {
        case .receiptsList:
            break
        case .simpleRecipe:
            navigateToSimpleRecipeScreen.next = true
        }
    }

    var receiptsListScreen: LazyView<ReceiptsListView> {
        return LazyView(
            ReceiptsListView(
                viewModel: delegate.makeReceiptsListViewModel()
            )
        )
    }

    var simpleRecipeScreen: LazyView<SimpleRecipeView> {
        return LazyView(
            SimpleRecipeView(viewModel: delegate.makeSimpleRecipeViewModel())
        )
    }

    var body: some View {
        NavigationView {
            VStack {
                receiptsListScreen
                Flow(state: navigateToSimpleRecipeScreen) {
                    simpleRecipeScreen
                }
            }
        }
    }
}
