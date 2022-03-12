import Foundation

final class TimerViewModel: ObservableObject {
    private let openTimerSelectionView: () -> Void

    init(openTimerSelectionView: @escaping () -> Void) {
        self.openTimerSelectionView = openTimerSelectionView
    }

    func onTimerTap() {
        openTimerSelectionView()
    }
}
