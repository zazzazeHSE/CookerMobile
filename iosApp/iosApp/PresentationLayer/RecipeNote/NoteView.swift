import SwiftUI

protocol NoteInputViewModel: ObservableObject {
    var inputText: String { get set }
    func didTapClose()
}

struct NoteView<ViewModel>: View where ViewModel: NoteInputViewModel {
    @ObservedObject var viewModel: ViewModel

    var body: some View {
        HStack {
            Spacer()
            VStack {
                VStack {
                    Text("Заметка")
                        .font(.title2)
                    TextEditor(text: $viewModel.inputText)
                        .font(.custom("Helvetica", size: 24))
                        .padding(.all)
                    closeButton
                }
                .padding(20)
            }
            .background(.white)
            .cornerRadius(38)
            .overlay(
                RoundedRectangle(cornerRadius: 38).stroke(Colors.orange, lineWidth: 1)
            )
            .padding(.vertical, 50)
            .onTapGesture { }
            Spacer()
        }
        .background(.ultraThinMaterial)
        .onTapGesture {
            viewModel.didTapClose()
        }
    }

    private var closeButton: some View {
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
    }
}

//struct NoteView_Previews: PreviewProvider {
//    static var previews: some View {
//        NoteView()
//    }
//}
