import SwiftUI
import shared

@main
struct iOSApp: App {
	var body: some Scene {
		WindowGroup {
            ReceiptsListView(
                router: ReceiptsRouter(
                    isPresented: .constant(false)
                ),
                viewModel: .init()
            )
		}
	}

	init() {
        KoinFactoryKt.doInitKoin()
    }
}
