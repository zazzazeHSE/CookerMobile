import Foundation
import SwiftUI
import shared

final class TimerViewModel: BaseViewModel<TimerState, TimerEffect> {
    @Published var timerModel: Model<shared.Timer> = .loading
    private let openTimerSelectionView: () -> Void
    private let closeTimeSelectionView: () -> Void

    private lazy var store: TimerStore = {
        let store = TimerStore()
        store.stateFlow.collect(collector: collector, completionHandler: { _, _ in })
        store.sideEffectsFlow.collect(collector: effectCollector, completionHandler: { _, _ in })
        return store
    }()

    init(
        openTimerSelectionView: @escaping () -> Void,
        closeTimeSelectionView: @escaping () -> Void
    ) {
        self.openTimerSelectionView = openTimerSelectionView
        self.closeTimeSelectionView = closeTimeSelectionView
        super.init()
    }

    func onTimerTap() {
        store.reduce(action: TimerAction.Tap())
    }

    override func didChangeState(_ state: TimerState?) {
        guard let state = state else { return }
        if state.timerResource.isData {
            timerModel = .data(state.timerResource.value!)
        }
        else if let error = state.timerResource.throwable {
            timerModel = .error(error.message ?? "Unexpected error")
        }
        else {
            timerModel = .loading
        }
    }

    override func didRecieveEffect(_ effect: TimerEffect?) {
        if effect is TimerEffect.ShowTimeSelector {
            openTimerSelectionView()
        }
        else if effect is TimerEffect.HideTimeSelector {
            closeTimeSelectionView()
        }
    }
}

extension TimerViewModel: TimeSelectionViewModel {
    func didTapStartButton(date: Date) {
        let calendar = Calendar.current
        store.reduce(
            action: TimerAction.StartTimer(
                timer: .init(
                    hours: Int32(calendar.component(.hour, from: date)),
                    minutes: Int32(calendar.component(.minute, from: date)),
                    seconds: Int32(calendar.component(.second, from: date))
                )
            )
        )
    }

    func didTapClose() {
        closeTimeSelectionView()
    }
}
