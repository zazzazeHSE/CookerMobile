import SwiftUI

struct TimeSelectionView: View {
    @State var hours: Date = .now
    @ObservedObject var viewModel: TimeSelectionViewModel
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
                            selection: $hours,
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
                viewModel.onBackgroundTap()
            }
        }
    }

    private var buttons: some View {
        HStack {
            Button(
                action: {
                    viewModel.onCancelButtonTap()
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

struct TimeSelectionView_Previews: PreviewProvider {
    static var previews: some View {
        TimeSelectionView(viewModel: .init {  })
    }
}
