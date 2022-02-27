import SwiftUI

struct SheetModifier: ViewModifier {

    @Binding var presentingView: AnyView?

    func body(content: Content) -> some View {
        content.sheet(
            isPresented: Binding(
                get: { self.presentingView != nil },
                set: { newValue in
                    if newValue == false {
                        self.presentingView = nil
                    }
                }
            )
        ) { self.presentingView }
    }
}
