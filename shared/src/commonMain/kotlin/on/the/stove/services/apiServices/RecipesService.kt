package on.the.stove.services.apiServices

import on.the.stove.dispatchers.ktorScope
import on.the.stove.models.RecipeModel
import on.the.stove.services.network.NetworkService
import on.the.stove.services.requestBuilders.RecipesRequestBuilder
import on.the.stove.services.responseModels.DefaultResponse

class RecipesService {
    private val networkService = NetworkService()
    suspend fun loadRecipes(category: Int, page: Int, callback: (Result<List<RecipeModel>>) -> Unit) {
        ktorScope {
            val builder = RecipesRequestBuilder(category, page)
            networkService.loadData<DefaultResponse<RecipeModel>>(builder).onSuccess { response ->
                callback(Result.success(response.body))
            }.onFailure { exception ->
                callback(Result.failure(exception))
            }
        }
    }
}