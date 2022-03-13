import Foundation
import shared

struct SimpleRecipe {
    let id: String
    let title: String
    let description: String
    let favourite: Bool
    let imageUrl: URL?
    let ingredients: [Ingredient]
    let steps: [Step]
}

struct Step {
    let id = UUID().uuidString
    var imageUrl: URL?
    var steps: [String]
}
