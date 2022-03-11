import SwiftUI

struct TimerView: View {
    @Binding var showTimeSelectionView: Bool
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
            showTimeSelectionView = true
        }
    }
}

struct TimerView_Previews: PreviewProvider {
    static var previews: some View {
        TimerView(showTimeSelectionView: .constant(false))
    }
}
