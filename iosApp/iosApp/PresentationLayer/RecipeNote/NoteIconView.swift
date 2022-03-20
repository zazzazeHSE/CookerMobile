import SwiftUI

protocol NoteIconViewModel: ObservableObject {
    func didTapOnIcon()
}

struct NoteIconView<ViewModel>: View where ViewModel: NoteIconViewModel {
    @ObservedObject var viewModel: ViewModel
    var body: some View {
        VStack {
            Image(systemName: "pencil")
                .resizable()
                .frame(width: 24, height: 24)
                .foregroundColor(Color.white)
                .padding(10)
        }
        .background(Colors.orange)
        .cornerRadius(17)
        .onTapGesture {
            viewModel.didTapOnIcon()
        }
    }
}

#if DEBUG
struct NoteIconView_Previews: PreviewProvider {
    static var previews: some View {
        NoteIconView(viewModel: NoteIconViewModel_Preview())
    }
}

private final class NoteIconViewModel_Preview: NoteIconViewModel {
    func didTapOnIcon() {  }
}
#endif
