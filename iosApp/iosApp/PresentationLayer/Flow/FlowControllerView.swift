import Foundation

import SwiftUI

protocol FlowControllerViewDelegate: AnyObject {
    func makeReceiptsListViewModel() -> ReceiptsViewModel
    func makeSimpleRecipeViewModel() -> SimpleRecipeViewModel
    func makeTimerViewModel() -> TimerViewModel
    func makeTimeSelectionViewModel() -> TimerViewModel
    func makeFavouritesReceiptsViewModel() -> FavouritesReceiptsViewModel
    func makeIngredientsCartViewModel() -> IngredientsCartViewModel
}

enum Screen {
    case any
    case receiptsList
    case favouritesList
    case simpleRecipe
    case timeSelectionView
}

struct FlowControllerView: View, FlowControllerViewProtocol {
    weak var delegate: FlowControllerViewDelegate!

    private let navigateToSimpleRecipeScreenFromMain = FlowState()
    private let navigateToSimpleRecipeScreenFromFavourites = FlowState()
    private let showTimerSelectionScreen = FlowState()

    init(modelDelegate: FlowControllerViewDelegate) {
        self.delegate = modelDelegate
    }

    func navigate(to navigateTo: Screen, from: Screen) {
        switch navigateTo {
        case .any:
            break
        case .receiptsList:
            break
        case .favouritesList:
            break
        case .simpleRecipe:
            if from == .receiptsList {
                navigateToSimpleRecipeScreenFromMain.next = true
            } else if from == .favouritesList {
                navigateToSimpleRecipeScreenFromFavourites.next = true
            }
        case .timeSelectionView:
            withAnimation { showTimerSelectionScreen.next = true }
        }
    }

    func close(_ screen: Screen) {
        switch screen {
        case .any:
            break
        case .receiptsList:
            break
        case .favouritesList:
            break
        case .simpleRecipe:
            break
        case .timeSelectionView:
            withAnimation { showTimerSelectionScreen.next = false }
        }
    }

    private var receiptsListScreen: LazyView<CategoriesReceiptsListView> {
        return LazyView(
            CategoriesReceiptsListView(
                viewModel: delegate.makeReceiptsListViewModel()
            )
        )
    }

    private var simpleRecipeScreen: LazyView<SimpleRecipeView> {
        return LazyView(
            SimpleRecipeView(viewModel: delegate.makeSimpleRecipeViewModel())
        )
    }

    private var timerView: LazyView<TimerView> {
        return LazyView(
            TimerView(viewModel: delegate.makeTimerViewModel())
        )
    }

    private var timerSelectionView: LazyView<TimeSelectionView<TimerViewModel>> {
        return LazyView(
            TimeSelectionView(viewModel: delegate.makeTimeSelectionViewModel())
        )
    }

    private var favouritesReceiptsScreen: LazyView<FavouritesReceiptsView> {
        return LazyView(
            FavouritesReceiptsView(viewModel: delegate.makeFavouritesReceiptsViewModel())
        )
    }

    private var ingredientsCartScreen: LazyView<IngredientsCartView> {
        return LazyView(
            IngredientsCartView(viewModel: delegate.makeIngredientsCartViewModel())
        )
    }

    var body: some View {
        ZStack(alignment: .bottomTrailing) {
            TabView {
                NavigationView {
                    VStack {
                        receiptsListScreen
                        Flow(state: navigateToSimpleRecipeScreenFromMain) {
                            simpleRecipeScreen
                        }
                    }
                }
                .tabItem {
                    Image(systemName: "list.bullet.rectangle.fill")
                    Text("Рецепты")
                }
                .tag(0)

                NavigationView {
                    VStack {
                        favouritesReceiptsScreen
                        Flow(state: navigateToSimpleRecipeScreenFromFavourites) {
                            simpleRecipeScreen
                        }
                    }
                }
                .tabItem {
                    Image(systemName: "heart")
                    Text("Понравившиеся")
                }
                .tag(1)

                NavigationView {
                    VStack {
                        ingredientsCartScreen
                    }
                }
                .tabItem {
                    Image(systemName: "cart")
                    Text("Корзина")
                }
                .tag(2)
            }
            .accentColor(Colors.orange)
            .onAppear {
                UITabBar.appearance().barTintColor = .white
            }
            timerView
                .padding()
                .padding(.bottom, 50)
            OverFlow(state: showTimerSelectionScreen) {
                timerSelectionView
            }
        }
    }
}
