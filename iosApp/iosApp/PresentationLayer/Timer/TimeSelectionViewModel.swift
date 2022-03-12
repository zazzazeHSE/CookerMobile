import Foundation

final class TimeSelectionViewModel: ObservableObject {
    private let onCloseTimeSelectionView: () -> Void

    init(onCloseTimeSelectionView: @escaping () -> Void) {
        self.onCloseTimeSelectionView = onCloseTimeSelectionView
    }

    func onCancelButtonTap() {
        onCloseTimeSelectionView()
    }

    func onBackgroundTap() {
        onCloseTimeSelectionView()
    }
}
