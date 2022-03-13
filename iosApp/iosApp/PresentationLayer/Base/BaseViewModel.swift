import Foundation
import SwiftUI
import shared

class BaseViewModel<State, Effect>: ObservableObject {
    lazy var collector: Observer = {
        let collector = Observer { [weak self] state in
            if let value = state as? State? {
                self?.didChangeState(value)
            }
        }
        return collector
    }()

    lazy var effectCollector: Observer = {
        let collector = Observer { [weak self] effect in
            if let value = effect as? Effect? {
                self?.didRecieveEffect(value)
            }
        }
        return collector
    }()

    func didChangeState(_ state: State?) {  }

    func didRecieveEffect(_ effect: Effect?) {  }
}
