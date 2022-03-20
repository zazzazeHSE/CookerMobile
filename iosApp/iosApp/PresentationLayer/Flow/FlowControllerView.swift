import Foundation

import SwiftUI

protocol FlowControllerViewDelegate: AnyObject {
    func makeReceiptsListViewModel() -> ReceiptsViewModel
    func makeSimpleRecipeViewModel() -> SimpleRecipeViewModel
    func makeTimerViewModel() -> TimerViewModel
    func makeTimeSelectionViewModel() -> TimerViewModel
    func makeFavouritesReceiptsViewModel() -> FavouritesReceiptsViewModel
    func makeIngredientsCartViewModel() -> IngredientsCartViewModel
    func makeNoteIconViewModel() -> NoteIconViewModelImpl
    func makeNoteInputViewModel() -> NoteInputViewModelImpl
    func makeSearchListViewModel() -> SearchListViewModelImpl
}

enum Screen {
    case any
    case receiptsList
    case favouritesList
    case simpleRecipe
    case timeSelectionView
    case noteInputView
}

struct FlowControllerView: View, FlowControllerViewProtocol {
    weak var delegate: FlowControllerViewDelegate!

    private let navigateToSimpleRecipeScreenFromMain = FlowState()
    private let navigateToSimpleRecipeScreenFromFavourites = FlowState()
    private let showTimerSelectionScreen = FlowState()
    private let showNoteIconView = FlowState()
    private let showNoteInputView = FlowState()

    init(modelDelegate: FlowControllerViewDelegate) {
        self.delegate = modelDelegate
    }

    func navigate(to navigateTo: Screen) {
        switch navigateTo {
        case .any:
            break
        case .receiptsList:
            break
        case .favouritesList:
            break
        case .simpleRecipe:
            navigateToSimpleRecipeScreenFromMain.next = true
            withAnimation { showNoteIconView.next = true }
        case .timeSelectionView:
            withAnimation { showTimerSelectionScreen.next = true }
        case .noteInputView:
            withAnimation { showNoteInputView.next = true }
        }
    }

    func close(_ screen: Screen) {
        switch screen {
        case .timeSelectionView:
            withAnimation { showTimerSelectionScreen.next = false }
        case .noteInputView:
            withAnimation { showNoteInputView.next = false }
        default: break
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

    private var noteIconView: LazyView<NoteIconView<NoteIconViewModelImpl>> {
        return LazyView(
            NoteIconView(viewModel: delegate.makeNoteIconViewModel())
        )
    }

    private var timerSelectionView: LazyView<TimeSelectionView<TimerViewModel>> {
        return LazyView(
            TimeSelectionView(viewModel: delegate.makeTimeSelectionViewModel())
        )
    }

    private var noteInputView: LazyView<NoteView<NoteInputViewModelImpl>> {
        return LazyView(
            NoteView(viewModel: delegate.makeNoteInputViewModel())
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

    private var searchScreen: LazyView<SearchListView<SearchListViewModelImpl>> {
        return LazyView(
            SearchListView(viewModel: delegate.makeSearchListViewModel())
        )
    }

    @State var tabBarVisible = false

    var body: some View {
        ZStack(alignment: .bottomTrailing) {
            NavigationView {
                VStack {
                    TabView {
                        NavigationView {
                            receiptsListScreen
                        }
                            .tabItem {
                                Image(systemName: "list.bullet.rectangle.fill")
                                Text("Рецепты")
                            }
                            .tag(0)

                        NavigationView {
                            searchScreen
                        }
                            .tabItem {
                                Image(systemName: "magnifyingglass")
                                Text("Поиск")
                            }
                            .tag(1)

                        NavigationView {
                            favouritesReceiptsScreen
                        }
                            .tabItem {
                                Image(systemName: "heart")
                                Text("Понравившиеся")
                            }
                            .tag(2)

                        NavigationView {
                            ingredientsCartScreen
                        }
                            .tabItem {
                                Image(systemName: "cart")
                                Text("Корзина")
                            }
                            .tag(3)
                    }
                        .accentColor(Colors.orange)
                        .onAppear {
                            UITabBar.appearance().barTintColor = .white
                            withAnimation {
                                tabBarVisible = true
                                showNoteIconView.next = false
                            }
                        }
                        .onWillDisappear {
                            withAnimation { tabBarVisible = false }
                        }

                    Flow(state: navigateToSimpleRecipeScreenFromMain) {
                        simpleRecipeScreen
                    }
                }
                .navigationBarHidden(true)
            }

            HStack {
                VisibleFlow(state: showNoteIconView) {
                    noteIconView
                }
                Spacer()
                timerView
            }
                .padding()
                .padding(.bottom, tabBarVisible ? 50 : 0)
            VisibleFlow(state: showTimerSelectionScreen) {
                timerSelectionView
            }
            VisibleFlow(state: showNoteInputView) {
                noteInputView
            }
        }
    }
}

struct WillDisappearHandler: UIViewControllerRepresentable {
    func makeCoordinator() -> WillDisappearHandler.Coordinator {
        Coordinator(onWillDisappear: onWillDisappear)
    }

    let onWillDisappear: () -> Void

    func makeUIViewController(context: UIViewControllerRepresentableContext<WillDisappearHandler>) -> UIViewController {
        context.coordinator
    }

    func updateUIViewController(_ uiViewController: UIViewController, context: UIViewControllerRepresentableContext<WillDisappearHandler>) {
    }

    typealias UIViewControllerType = UIViewController

    class Coordinator: UIViewController {
        let onWillDisappear: () -> Void

        init(onWillDisappear: @escaping () -> Void) {
            self.onWillDisappear = onWillDisappear
            super.init(nibName: nil, bundle: nil)
        }

        required init?(coder: NSCoder) {
            fatalError("init(coder:) has not been implemented")
        }

        override func viewWillDisappear(_ animated: Bool) {
            super.viewWillDisappear(animated)
            onWillDisappear()
        }
    }
}

struct WillDisappearModifier: ViewModifier {
    let callback: () -> Void

    func body(content: Content) -> some View {
        content
            .background(WillDisappearHandler(onWillDisappear: callback))
    }
}

extension View {
    func onWillDisappear(_ perform: @escaping () -> Void) -> some View {
        self.modifier(WillDisappearModifier(callback: perform))
    }
}
