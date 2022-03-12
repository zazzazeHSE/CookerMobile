import SwiftUI
import Foundation

struct SimpleReceiptView: View {
    let receipt: SimpleRecipe
    @State private var selectedSegment: PickerItems = .description
    var body: some View {
        VStack(alignment: .leading) {
            ReceiptImageView(
                model: .init(
                    imageURL: receipt.imageUrl,
                    isLiked: receipt.favourite
                )
            )
            titleView
            pickerView
            switch selectedSegment {
            case .description:
                descriptionView
            case .ingredients:
                ingredientsView
            case .recipe:
                recipeView
            }
            Spacer()
        }
        .padding(.horizontal)
    }

    private var titleView: some View {
        Text(receipt.title)
            .font(
                .system(
                    size: 25,
                    weight: .semibold,
                    design: .default
                )
            )
    }

    private var pickerView: some View {
        SegmentedPicker(
            items: PickerItems.pickerTitles,
            selection: .init(
                get: { selectedSegment.rawValue },
                set: { newValue in
                    let item = PickerItems(rawValue: newValue)!
                    selectedSegment = item
                }
            )
        )
        .pickerStyle(.segmented)
        .background(Color.white)
    }

    private var descriptionView: some View {
        Text(receipt.description)
            .font(.system(size: 15))
            .lineLimit(nil)
            .lineSpacing(1.57)
    }

    private var ingredientsView: some View {
        VStack {
            ForEach(receipt.ingredients, id: \.name) { ingredient in
                HStack {
                    Text(ingredient.name)
                    Spacer()
                    Text(ingredient.value)
                }
            }
        }
    }

    private var recipeView: some View {
        VStack {
            ForEach(receipt.steps, id: \.id) { step in
                AsyncImage(url: step.imageUrl)
                ForEach(step.steps, id: \.self) { recipeStep in
                    Text(recipeStep)
                }
            }
        }
    }

    private enum PickerItems: Int {
        static let pickerTitles = [
            "Описание",
            "Ингредиенты",
            "Рецепт"
        ]

        case description = 0
        case ingredients = 1
        case recipe = 2
    }
}

#if DEBUG
struct SimpleReceiptView_Previews: PreviewProvider {
    static var previews: some View {
        SimpleReceiptView(
            receipt: .init(
                id: "",
                title: "Пряный суп из батата",
                description: "dfa",
                favourite: true,
                imageUrl: URL(string: "https://media.healthkurs.ru/wp-content/uploads/2021/07/sladkij-kartofel.jpg")!,
                ingredients: [],
                steps: []
            )
        )
            .previewDevice(PreviewDevice(rawValue: "iPhone 13 Pro Max"))
    }
}
#endif
