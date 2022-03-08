package on.the.stove.services.apiServices

import on.the.stove.dispatchers.ktorScope
import on.the.stove.dto.Recipe
import on.the.stove.services.network.NetworkService
import on.the.stove.services.requestBuilders.RecipesRequestBuilder
import on.the.stove.services.responseModels.DefaultResponse

internal class RecipesService {
    private val networkService = NetworkService()

    suspend fun loadRecipes(category: Int, page: Int, callback: (Result<List<Recipe>>) -> Unit) {
        ktorScope {
            val builder = RecipesRequestBuilder(category, page)
            networkService.loadData<DefaultResponse<Recipe>>(builder).onSuccess { response ->
                callback(Result.success(response.body))
            }.onFailure { exception ->
                callback(Result.failure(exception))
            }
        }
    }
}