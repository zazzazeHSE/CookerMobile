import SwiftUI

struct IngredientsCartView: View {
    @ObservedObject var viewModel: IngredientsCartViewModel
    var body: some View {
        ScrollView {
            VStack(alignment: .center) {
                switch viewModel.ingredientsModel {
                case .loading: ProgressView()
                case .error(let error): Text(error)
                case .data(let data):
                    if data.isEmpty {
                        Text("Нет продуктов в корзине")
                    }
                }
                IngredientsListView(viewModel: viewModel)
                Spacer()
            }
            .padding(.horizontal, 20)
            .padding(.bottom, 40)
        }
        .navigationTitle("Корзина продуктов")
    }
}

#if DEBUG
struct IngredientsCartView_Previews: PreviewProvider {
    static var previews: some View {
        IngredientsCartView(viewModel: .init())
    }
}
#endif
