import Foundation
import SwiftUI
import shared

class BaseViewModel<State>: ObservableObject {
    lazy var collector: Observer = {
        let collector = Observer { [weak self] value in
            if let value = value as? State? {
                self?.didChangeState(value)
            }
        }
        return collector
    }()

    func didChangeState(_ state: State?) {  }
}
