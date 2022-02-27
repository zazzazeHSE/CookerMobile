import SwiftUI

struct NavigationModifier: ViewModifier {

    @Binding var presentingView: AnyView?

    func body(content: Content) -> some View {
        content.background(
            NavigationLink(
                destination: self.presentingView,
                isActive: Binding(
                    get: { self.presentingView != nil },
                    set: { newValue in
                        if newValue == false {
                            self.presentingView = nil
                        }
                    }
                )
            ) { EmptyView() }
        )
    }
}
