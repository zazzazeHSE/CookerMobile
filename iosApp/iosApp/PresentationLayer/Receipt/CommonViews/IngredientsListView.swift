import SwiftUI
import shared

protocol IngredientsListViewModel: ObservableObject {
    var model: [Ingredient] { get }
    func didTapOnSelectIngredient(_ ingredient: Ingredient)
}

struct IngredientsListView<ViewModel>: View where ViewModel: IngredientsListViewModel {
    @ObservedObject var viewModel: ViewModel
    var body: some View {
        VStack {
            ForEach(viewModel.model, id: \.id) { ingredient in
                HStack {
                    Text(ingredient.name)
                    Spacer()
                    Text(ingredient.value)
                    toggleView(active: ingredient.inCart)
                        .onTapGesture {
                            viewModel.didTapOnSelectIngredient(ingredient)
                        }
                }
            }
        }
    }

    @ViewBuilder private func toggleView(active: Bool) -> some View {
        Image(systemName: active ? "checkmark.circle.fill" : "circle")
            .resizable()
            .frame(width: 24, height: 24)
            .foregroundColor(active ? Colors.orange : .gray)
            .font(.system(size: 20, weight: .bold, design: .default))
    }
}

#if DEBUG
struct IngredientsListView_Previews: PreviewProvider {
    static var previews: some View {
        IngredientsListView(viewModel: IngredientsListPreviewViewModel())
    }
}

private final class IngredientsListPreviewViewModel: IngredientsListViewModel {
    var model: [Ingredient] = []

    func didTapOnSelectIngredient(_ ingredient: Ingredient) {  }
}
#endif
