import SwiftUI
import shared

struct TimerView: View {
    @ObservedObject var viewModel: TimerViewModel
    var body: some View {
        VStack {
            switch viewModel.timerModel {
            case .data(let timer): makeTimer(timer)
            default: icon
            }
        }
        .background(Colors.orange)
        .cornerRadius(17)
        .onTapGesture {
            viewModel.onTimerTap()
        }
    }

    private var icon: some View {
        Image(systemName: "timer")
            .resizable()
            .frame(width: 24, height: 24)
            .foregroundColor(Color.white)
            .padding(10)
    }

    @ViewBuilder private func makeTimer(_ timer: shared.Timer) -> some View {
        HStack {
            Text("\(String(format: "%02d", timer.hours)):\(String(format: "%02d", timer.minutes)):\(String(format: "%02d", timer.seconds))")
        }
        .foregroundColor(.white)
        .padding(10)
    }
}

struct TimerView_Previews: PreviewProvider {
    static var previews: some View {
        TimerView(viewModel: .init(openTimerSelectionView: {}, closeTimeSelectionView: {}))
    }
}
