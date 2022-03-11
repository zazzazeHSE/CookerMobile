import SwiftUI
import shared

@main
struct iOSApp: App {
    private let controller: FlowController
    private let view: FlowControllerView
    init() {
        let controller = FlowController()
        let view = FlowControllerView(modelDelegate: controller)
        controller.view = view
        self.controller = controller
        self.view = view
    }
	var body: some Scene {
		WindowGroup {
            view
		}
	}

	init() {
        KoinFactoryKt.doInitKoin()
    }
}
