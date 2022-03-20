import Foundation

struct ReceiptsListModel {
    var receipts: [Receipt]?
    var isPaginationLoading: Bool
    var isFull: Bool
}

struct CategoryModel {
    let id: String
    let title: String
    let current: Bool
}

extension CategoryModel: Changeable {
    init(copy: ChangeableWrapper<CategoryModel>) {
        self.id = copy.id
        self.title = copy.title
        self.current = copy.current
    }
}
