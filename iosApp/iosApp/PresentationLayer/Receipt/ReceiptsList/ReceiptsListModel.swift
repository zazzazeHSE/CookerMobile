import Foundation

struct ReceiptsModel {
    var listModel: ReceiptsList
    var isLoading: Bool
    var error: String?
}

struct ReceiptsList {
    var receipts: [Receipt]?
    var isFull: Bool
}

extension ReceiptsModel: Changeable {
    init(copy: ChangeableWrapper<ReceiptsModel>) {
        self.listModel = copy.listModel
        self.isLoading = copy.isLoading
        self.error = copy.error
    }
}

extension ReceiptsList: Changeable {
    init(copy: ChangeableWrapper<ReceiptsList>) {
        self.receipts = copy.receipts
        self.isFull = copy.isFull
    }
}
