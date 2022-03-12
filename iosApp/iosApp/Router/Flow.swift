import Foundation
import SwiftUI

class FlowState: ObservableObject {
    @Published var next: Bool = false
}

struct Flow<Content>: View where Content: View {
    @ObservedObject var state: FlowState
    var content: Content
    var body: some View {
        NavigationLink(
            destination: VStack { content },
            isActive: $state.next
        ) {
            EmptyView()
        }
    }
    init(state: FlowState, @ViewBuilder content: () -> Content) {
        self.state = state
        self.content = content()
    }
}

struct LazyView<Content: View>: View {
    let build: () -> Content
    init(_ build: @autoclosure @escaping () -> Content) {
        self.build = build
    }
    var body: Content {
        build()
    }
}
