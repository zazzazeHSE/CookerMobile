import SwiftUI

protocol ReceiptsListViewModel: ObservableObject {
    var model: Model<ReceiptsListModel> { get }
    func onRecipeTap(_ receipt: Receipt)
    func onScrollToLast()
    func onRecipeLikeButtonTap(_ receipt: Receipt)
}

struct ReceiptsListView<VM>: View where VM: ReceiptsListViewModel {
    @ObservedObject var viewModel: VM

    var body: some View {
        switch viewModel.model {
        case .data(let data): receiptsList(model: data)
        case .error(let error): HStack(alignment: .center) {
            Text(error)
                .foregroundColor(.red)
        }
        case .loading: ProgressView()
        }
    }

    @ViewBuilder private func receiptsList(model: ReceiptsListModel) -> some View {
        if
            let receipts = model.receipts,
            receipts.isEmpty
        {
            Text("Нет доступных рецептов")
        }
        List {
            ForEach(model.receipts ?? [], id: \.id) { receipt in
                ReceiptListItemView(
                    receipt: receipt,
                    onLikeButtonTap: { viewModel.onRecipeLikeButtonTap(receipt) }
                )
                    .padding(.horizontal, 10)
                    .padding(.vertical, 20)
                    .onAppear {
                        if receipt == model.receipts?.last {
                            viewModel.onScrollToLast()
                        }
                    }
                    .background(.clear)
                    .onTapGesture {
                        viewModel.onRecipeTap(receipt)
                    }
            }
            .listRowInsets(EdgeInsets())
            .listRowSeparator(.hidden)
            .listRowBackground(Color.white)
            if model.isFull == false {
                HStack(alignment: .center) {
                    Spacer()
                    ProgressView()
                    Spacer()
                }
                .listRowInsets(EdgeInsets())
                .listRowSeparator(.hidden)
                .listRowBackground(Color.white)
            }
        }
        .safeAreaInset(edge: .bottom) {
            EmptyView().frame(width: 0, height: 50)
        }
        .listStyle(PlainListStyle())
        .background(.white)
    }
}

#if DEBUG
struct ReceiptsListView_Previews: PreviewProvider {
    static var previews: some View {
        ReceiptsListView(
            viewModel: ReceiptsListPreviewViewModel()
        )
    }
}

private final class ReceiptsListPreviewViewModel: ReceiptsListViewModel {
    var model: Model<ReceiptsListModel> = .loading

    func onRecipeTap(_ receipt: Receipt) {

    }

    func onScrollToLast() {

    }

    func onRecipeLikeButtonTap(_ receipt: Receipt) {

    }


}
#endif
