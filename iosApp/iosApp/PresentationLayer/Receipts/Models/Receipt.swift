import Foundation

struct Receipt {
    let id = UUID()
    let title: String
    let imageURL: URL
    let rating: Double
    let ratesCount: Int
    let favourite: Bool
}
