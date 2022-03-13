import SwiftUI

protocol TimeSelectionViewModel: ObservableObject {
    func didTapStartButton(date: Date)
    func didTapClose()
}

struct TimeSelectionView<ViewModel>: View where ViewModel: TimeSelectionViewModel {
    @State var selectedTime: Date = Date(timeIntervalSince1970: 0)
    @ObservedObject var viewModel: ViewModel
    var body: some View {
        GeometryReader { reader in
            HStack {
                Spacer()
                VStack {
                    Spacer()
                    VStack {
                        Text("Таймер")
                            .padding(.top, 28)
                        DatePicker(
                            "",
                            selection: $selectedTime,
                            displayedComponents: .hourAndMinute
                        )
                            .datePickerStyle(.wheel)
                            .frame(width: reader.size.width - 34, height: nil)
                            .clipped()
                        buttons
                            .padding(.bottom, 57)
                    }
                    .background(.white)
                    .cornerRadius(38)
                    .overlay(
                        RoundedRectangle(cornerRadius: 38).stroke(Colors.orange, lineWidth: 1)
                    )
                    Spacer()
                }
                Spacer()
            }
            .background(.ultraThinMaterial)
            .onTapGesture {
                viewModel.didTapClose()
            }
        }
    }

    private var buttons: some View {
        HStack {
            Button(
                action: {
                    viewModel.didTapClose()
                }
            ) {
                Text("Закрыть")
                    .padding(.vertical, 13)
                    .padding(.horizontal, 20)
                    .foregroundColor(.black)
            }
            .background(.white)
            .cornerRadius(28.5)
            Button(
                action: {
                    viewModel.didTapStartButton(date: selectedTime)
                }
            ) {
                Text("Старт")
                    .padding(.vertical, 13)
                    .padding(.horizontal, 20)
                    .foregroundColor(.white)
            }
            .background(Colors.orange)
            .cornerRadius(28.5)
        }
    }
}

#if DEBUG
struct TimeSelectionView_Previews: PreviewProvider {
    static var previews: some View {
        TimeSelectionView(viewModel: TimeSelectionPreviewModel())
    }
}

private class TimeSelectionPreviewModel: TimeSelectionViewModel {
    func didTapStartButton(date: Date) {

    }

    func didTapClose() {

    }
}
#endif
