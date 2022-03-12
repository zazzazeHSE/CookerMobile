import Foundation

import SwiftUI

protocol FlowControllerViewDelegate: AnyObject {
    func makeReceiptsListViewModel() -> ReceiptsViewModel
    func makeSimpleRecipeViewModel() -> SimpleRecipeViewModel
    func makeTimerViewModel() -> TimerViewModel
    func makeTimeSelectionViewModel() -> TimeSelectionViewModel
}

enum Navigate {
    case receiptsList
    case simpleRecipe
    case timeSelectionView
}

struct FlowControllerView: View, FlowControllerViewProtocol {
    weak var delegate: FlowControllerViewDelegate!

    private let navigateToSimpleRecipeScreen = FlowState()
    private let showTimerSelectionScreen = FlowState()

    init(modelDelegate: FlowControllerViewDelegate) {
        self.delegate = modelDelegate
    }

    func navigate(to navigateTo: Navigate) {
        switch navigateTo {
        case .receiptsList:
            break
        case .simpleRecipe:
            navigateToSimpleRecipeScreen.next = true
        case .timeSelectionView:
            withAnimation { showTimerSelectionScreen.next = true }
        }
    }

    func close(_ screen: Navigate) {
        switch screen {
        case .receiptsList:
            break
        case .simpleRecipe:
            break
        case .timeSelectionView:
            withAnimation { showTimerSelectionScreen.next = false }
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

    var timerView: LazyView<TimerView> {
        return LazyView(
            TimerView(viewModel: delegate.makeTimerViewModel())
        )
    }

    var timerSelectionView: LazyView<TimeSelectionView> {
        return LazyView(
            TimeSelectionView(viewModel: delegate.makeTimeSelectionViewModel())
        )
    }

    var body: some View {
        ZStack(alignment: .bottomTrailing) {
            TabView {
                NavigationView {
                    VStack {
                        receiptsListScreen
                        Flow(state: navigateToSimpleRecipeScreen) {
                            simpleRecipeScreen
                        }
                    }
                }
                .tabItem {
                    Image(systemName: "list.bullet.rectangle.fill")
                    Text("Рецепты")
                }
                .tag(0)
            }
            .accentColor(Colors.orange)
            .onAppear {
                UITabBar.appearance().barTintColor = .white
            }
            timerView
                .padding()
                .padding(.bottom, 40)
            OverFlow(state: showTimerSelectionScreen) {
                timerSelectionView
            }
        }
    }
}
