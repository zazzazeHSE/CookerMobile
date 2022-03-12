import Foundation
import shared

typealias Collector = Kotlinx_coroutines_coreFlowCollector

class Observer: Collector {
    let callback:(Any?) -> Void

    init(callback: @escaping (Any?) -> Void) {
        self.callback = callback
    }

    func emit(value: Any?, completionHandler: @escaping (KotlinUnit?, Error?) -> Void) {
        callback(value)
        completionHandler(KotlinUnit(), nil)
    }
}
