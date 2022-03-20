package on.the.stove.presentation.searchRecipes

import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import on.the.stove.database.AppDatabaseRepository
import on.the.stove.presentation.BaseStore
import on.the.stove.services.network.RecipesApi
import org.koin.core.component.inject

class SearchRecipesStore :
    BaseStore<SearchRecipesState, SearchRecipesAction, SearchRecipesEffect>() {

    private val recipesApi: RecipesApi by inject()
    private val appDatabaseRepository: AppDatabaseRepository by inject()

    override val stateFlow: MutableStateFlow<SearchRecipesState> =
        MutableStateFlow(SearchRecipesState())
    override val sideEffectsFlow: MutableSharedFlow<SearchRecipesEffect> = MutableSharedFlow()

    override suspend fun reduce(action: SearchRecipesAction, initialState: SearchRecipesState) {
        when (action) {

        }
    }
}