package on.the.stove.presentation.favourites

import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import on.the.stove.core.toResource
import on.the.stove.database.AppDatabaseRepository
import on.the.stove.dispatchers.ioDispatcher
import on.the.stove.presentation.BaseStore
import org.koin.core.component.inject

class FavouritesStore : BaseStore<FavouritesState, FavouritesAction, FavouritesEffect>() {

    private val appDatabaseRepository: AppDatabaseRepository by inject()

    override val stateFlow: MutableStateFlow<FavouritesState> =
        MutableStateFlow(FavouritesState())
    override val sideEffectsFlow: MutableSharedFlow<FavouritesEffect> = MutableSharedFlow()

    override suspend fun reduce(action: FavouritesAction, initialState: FavouritesState) {
        when (action) {
            is FavouritesAction.Init -> {
                scope.launch(ioDispatcher) {
                    appDatabaseRepository.observeAllFavouritesRecipes()
                        .collect { dbRecipes ->
                            updateState { state ->
                                state.copy(
                                    recipesResource = dbRecipes.toResource()
                                )
                            }
                        }
                }
            }
            is FavouritesAction.Like -> {
                appDatabaseRepository.removeFavouriteRecipe(action.id)
            }
        }
    }
}