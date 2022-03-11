import SwiftUI

struct TabBarView: View {
    @State var showTimerView: Bool = false
    var body: some View {
        let timerBiding = Binding<Bool>.init(
            get: { showTimerView },
            set: { newValue in withAnimation { showTimerView = newValue } }
        )
        return ZStack(alignment: .bottomTrailing) {
            TabView {
                ReceiptsView(
                    router: ReceiptsRouter(
                        isPresented: .constant(false)
                    ),
                    viewModel: .init()
                )
                    .tabItem {
                        Image(systemName: "list.bullet.rectangle.fill")
                        Text("Рецепты")
                    }
                    .tag(0)
            }
            .accentColor(Colors.orange)
            .onAppear {
                UITabBar.appearance().barTintColor = .white
            }
            TimerView(
                showTimeSelectionView: timerBiding
            )
                .padding()
                .padding(.bottom, 40)
            if showTimerView {
                TimeSelectionView {
                    withAnimation { showTimerView = false }
                }
            }
        }
    }
}

struct TabBarView_Previews: PreviewProvider {
    static var previews: some View {
        TabBarView()
    }
}
