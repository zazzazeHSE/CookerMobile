import Foundation

struct Receipt {
    let id = UUID()
    let title: String
    let imageURL: URL
    let rating: Double
    let ratesCount: Int
    let favourite: Bool
    let description: String
    let ingredients: [Ingredient]
    let recipe: Recipe
}

struct Ingredient {
    let title: String
    let value: String
}


struct Recipe {
    let steps: [RecipeStep]
}

struct RecipeStep {
    let index: Int
    let description: String
}
