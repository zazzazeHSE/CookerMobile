package on.the.stove.presentation.recipes

import kotlinx.coroutines.launch
import on.the.stove.models.RecipeModel
import on.the.stove.presentation.BasePresenter
import on.the.stove.presentation.ViewResource
import on.the.stove.services.apiServices.RecipesService
import kotlinx.coroutines.*
import on.the.stove.dispatchers.ioDispatcher

class RecipesPresenter: BasePresenter<IRecipesView>(ioDispatcher), IRecipesPresenter {
    private val recipesService = RecipesService()
    private var currentCategory = CurrentCategory(id = 0, batch = 0, emptyList())

    override fun loadNextBatch(category: Int?) {
        val categoryForLoad = getCategoryById(category)

        scope.launch {
            recipesService.loadRecipes(categoryForLoad.id, categoryForLoad.batch + 1) { result ->
                result.onSuccess { model ->
                    currentCategory = currentCategory.copy(batch = categoryForLoad.batch + 1)
                    val models = currentCategory.loadedModels + model
                    val viewModel = RecipesViewModel(models, model.count() == 0)
                    uiScope.launch {
                        view?.updateWithResource(ViewResource.Data(viewModel))
                    }
                }.onFailure { exception ->
                    uiScope.launch {
                        view?.updateWithResource(ViewResource.Error(exception))
                    }
                }
            }
        }
    }

    private fun getCategoryById(id: Int?): CurrentCategory {
        if (id != currentCategory.id && id != null) {
            currentCategory = CurrentCategory(id, 0, emptyList())
        }

        return currentCategory
    }

    private data class CurrentCategory(
        val id: Int,
        val batch: Int,
        val loadedModels: List<RecipeModel>
    )
}