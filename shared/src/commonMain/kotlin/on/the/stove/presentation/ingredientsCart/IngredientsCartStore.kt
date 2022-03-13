package on.the.stove.presentation.ingredientsCart

import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import on.the.stove.core.toResource
import on.the.stove.database.AppDatabaseRepository
import on.the.stove.dispatchers.ioDispatcher
import on.the.stove.dto.Ingredient
import on.the.stove.presentation.BaseStore
import org.koin.core.component.inject

class IngredientsCartStore : BaseStore<IngredientsCartState, IngredientsCartAction, IngredientsCartEffect>()  {
    override val stateFlow: MutableStateFlow<IngredientsCartState> = MutableStateFlow(IngredientsCartState())
    override val sideEffectsFlow: MutableSharedFlow<IngredientsCartEffect> = MutableSharedFlow()
    private val appDatabaseRepository: AppDatabaseRepository by inject()

    override suspend fun reduce(action: IngredientsCartAction, initialState: IngredientsCartState) {
        when (action) {
            is IngredientsCartAction.Init -> {
                observeIngredients()
            }
            is IngredientsCartAction.Select -> {
                if (action.ingredient.inCart) {
                    appDatabaseRepository.removeIngredient(action.ingredient)
                } else {
                    appDatabaseRepository.addIngredient(action.ingredient)
                }
            }
        }
    }

    private fun observeIngredients() = scope.launch(ioDispatcher) {
        appDatabaseRepository.observeAllIngredients()
            .collect { ingredientsList ->
                updateState { state ->
                    state.copy(
                        ingredientsCartResource = ingredientsList.map {
                            it.copy(inCart = true)
                        }.toResource()
                    )
                }
            }
    }

}