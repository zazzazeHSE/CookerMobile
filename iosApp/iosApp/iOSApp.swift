import SwiftUI

@main
struct iOSApp: App {
	var body: some Scene {
		WindowGroup {
            ReceiptsView(
                router: ReceiptsRouter(
                    isPresented: .constant(false)
                ),
                viewModel: .init()
            )
		}
	}
}
