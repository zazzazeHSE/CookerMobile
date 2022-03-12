import Foundation

struct SimpleRecipe {
    let id: String
    let title: String
    let description: String
    let favourite: Bool
    let imageUrl: URL
    let ingredients: [Ingredient]
    let steps: [Step]
}

struct Ingredient {
    let id = UUID().uuidString
    let name: String
    let value: String
}

struct Step {
    let id = UUID().uuidString
    var imageUrl: URL?
    var steps: [String]
}
