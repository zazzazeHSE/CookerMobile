import SwiftUI

struct TimerView: View {
    @ObservedObject var viewModel: TimerViewModel
    var body: some View {
        VStack {
            Image(systemName: "timer")
                .resizable()
                .frame(width: 24, height: 24)
                .foregroundColor(Color.white)
                .padding(10)
        }
        .background(Colors.orange)
        .cornerRadius(17)
        .onTapGesture {
            viewModel.onTimerTap()
        }
    }
}

struct TimerView_Previews: PreviewProvider {
    static var previews: some View {
        TimerView(viewModel: .init { })
    }
}
