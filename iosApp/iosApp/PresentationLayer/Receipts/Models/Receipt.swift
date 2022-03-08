import Foundation

struct Receipt {
    let id: String
    let title: String
    let imageURL: URL
    let favourite: Bool
}

extension Receipt: Equatable {
    static func == (lhs: Receipt, rhs: Receipt) -> Bool {
        lhs.id == rhs.id
    }
}
