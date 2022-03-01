import SwiftUI
import Foundation

struct SimpleReceiptView: View {
    let receipt: Receipt
    @State private var selectedSegment: PickerItems = .description
    var body: some View {
        VStack(alignment: .leading) {
            ReceiptImageView(
                model: .init(
                    imageURL: receipt.imageURL,
                    isLiked: receipt.favourite
                )
            )
            titleView
            pickerView
            pickerSelectionBasedView
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

    private var pickerSelectionBasedView: some View {
        switch selectedSegment {
        case .description:
            return descriptionView
        case .ingredients:
            return ingredientsView
        case .recipe:
            return recipeView
        }
    }

    private var descriptionView: some View {
        Text(receipt.description)
            .font(.system(size: 15))
            .lineLimit(nil)
            .lineSpacing(1.57)
    }

    private var ingredientsView: some View {
        VStack {
            ForEach(receipt.ingredients, id: \.title) { ingredient in
                HStack {
                    Text(ingredient.title)
                    Spacer()
                    Text(ingredient.value)
                }
            }
        }
    }

    private var recipeView: some View {
        VStack {
            ForEach(receipt.recipe.steps, id: \.index) { step in
                Text("\(step.index). ")
                    .foregroundColor(Colors.orange) +
                 (Text(step.description).lineLimit(nil) as! Text)
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
                title: "Пряный суп из батата",
                imageURL: URL(string: "https://media.healthkurs.ru/wp-content/uploads/2021/07/sladkij-kartofel.jpg")!,
                rating: 4.5,
                ratesCount: 60,
                favourite: true,
                description: "sda",
                ingredients: [.init(title: "Творог 5%", value: "400 г.")],
                recipe: .init(steps: [.init(index: 1, description: "Почистить")])
            )
        )
            .previewDevice(PreviewDevice(rawValue: "iPhone 13 Pro Max"))
    }
}
#endif
