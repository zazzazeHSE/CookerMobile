import SwiftUI
import Foundation
#if DEBUG
import shared
#endif

struct SimpleRecipeView: View {
    @ObservedObject var viewModel: SimpleRecipeViewModel
    var body: some View {
        switch viewModel.recipeModel {
        case .error(let errorText): Text(errorText)
        case .loading: ProgressView()
        case .data(let recipe): RecipeView(
            ingredientsViewModel: viewModel,
            recipe: recipe,
            onLikeButtonTap: { viewModel.didTapOnLikeForReceipt(recipe) }
        )
        }
    }
}

private struct RecipeView<ViewModel>: View where ViewModel: IngredientsListViewModel {
    let ingredientsViewModel: ViewModel
    let recipe: SimpleRecipe
    let onLikeButtonTap: () -> Void
    @State private var selectedSegment: PickerItems = .description
    var body: some View {
        ScrollView {
            VStack(alignment: .leading) {
                ReceiptImageView(
                    model: .init(
                        imageURL: recipe.imageUrl,
                        isLiked: recipe.favourite
                    ),
                    onLikeButtonTap: onLikeButtonTap
                )
                titleView
                pickerView
                    .padding(.bottom, 20)
                VStack {
                    switch selectedSegment {
                    case .description:
                        descriptionView
                    case .ingredients:
                        ingredientsView
                    case .recipe:
                        recipeView
                    }
                }
                .padding(.bottom, 60)
                Spacer()
            }
            .padding(.horizontal)
        }
    }

    private var titleView: some View {
        Text(recipe.title)
            .font(
                .system(
                    size: 25,
                    weight: .semibold,
                    design: .default
                )
            )
    }

    private var pickerView: some View {
        Picker("", selection: $selectedSegment) {
            ForEach(PickerItems.allCases, id: \.self) { item in
                Text(item.rawValue)
            }
        }
        .pickerStyle(.segmented)
        .background(Color.white)
        .foregroundColor(Colors.orange)
    }

    private var descriptionView: some View {
        Text(recipe.description)
            .font(.system(size: 15))
            .lineLimit(nil)
            .lineSpacing(1.57)
    }

    private var ingredientsView: some View {
        IngredientsListView(viewModel: self.ingredientsViewModel)
    }

    private var recipeView: some View {
        VStack(alignment: .leading, spacing: 20) {
            ForEach(recipe.steps, id: \.id) { step in
                AsyncImage(
                    url: step.imageUrl,
                    scale: 1.0,
                    content: { image in
                        image
                            .resizable()
                            .aspectRatio(contentMode: .fill)
                    },
                    placeholder: {
                        ProgressView()
                    }
                )
                    .frame(minWidth: 100, minHeight: 165, maxHeight: 165)
                    .cornerRadius(18)
                ForEach(step.steps, id: \.self) { recipeStep in
                    Text(recipeStep)
                        .padding(.top, 10)
                }
            }
        }
    }

    private enum PickerItems: String, CaseIterable, Hashable {
        case description = "Описание"
        case ingredients = "Ингредиенты"
        case recipe = "Рецепт"
    }
}

#if DEBUG
struct SimpleReceiptView_Previews: PreviewProvider {
    static var previews: some View {
        RecipeView(
            ingredientsViewModel: SimpleReceiptViewIngredientsViewModel(),
            recipe: .init(
                id: "",
                title: "Пряный суп из батата",
                description: "dfa",
                favourite: true,
                imageUrl: URL(string: "https://media.healthkurs.ru/wp-content/uploads/2021/07/sladkij-kartofel.jpg")!,
                ingredients: [],
                steps: []
            ),
            onLikeButtonTap: {}
        )
            .previewDevice(PreviewDevice(rawValue: "iPhone 13 Pro Max"))
    }

    private class SimpleReceiptViewIngredientsViewModel: IngredientsListViewModel {
        var model: [Ingredient] = []

        func didTapOnSelectIngredient(_ ingredient: Ingredient) {

        }


    }
}
#endif
