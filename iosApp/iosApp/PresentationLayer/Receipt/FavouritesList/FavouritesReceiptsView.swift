import SwiftUI

struct FavouritesReceiptsView: View {
    @ObservedObject var viewModel: FavouritesReceiptsViewModel

    var body: some View {
        VStack {
            ReceiptsListView(viewModel: viewModel)
        }
            .navigationTitle("Понравившиеся рецепты")
    }
}

struct FavouritesReceiptsView_Previews: PreviewProvider {
    static var previews: some View {
        FavouritesReceiptsView(
            viewModel: .init { _ in }
        )
    }
}
