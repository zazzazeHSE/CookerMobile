package on.the.stove.services.network

import io.ktor.client.request.*
import kotlinx.coroutines.withContext
import on.the.stove.dispatchers.ktorDispatcher
import on.the.stove.dto.DetailedRecipe
import on.the.stove.dto.Recipe
import on.the.stove.services.responseModels.Response

private const val BASE_URL = "http://195.2.80.162:80"

internal interface RecipesApi {

    suspend fun getRecipesList(page: Int?, category: Int?): Result<List<Recipe>>
    suspend fun getDetailedRecipe(id: String): Result<DetailedRecipe>
}

internal class RecipeApiImpl : RecipesApi {

    private val client = NetworkService.httpClient

    override suspend fun getRecipesList(page: Int?, category: Int?): Result<List<Recipe>> {
        return withContext(ktorDispatcher) {
            runCatching<Response<List<Recipe>>> {
                client.get("$BASE_URL/recipes") {
                    parameter("page", page)
                    parameter("category", category)
                }
            }.map { it.payload }
        }
    }

    override suspend fun getDetailedRecipe(id: String): Result<DetailedRecipe> {
        return withContext(ktorDispatcher) {
            runCatching<Response<DetailedRecipe>> {
                client.get("$BASE_URL/recipe/show") {
                    parameter("id", id)
                }
            }.map { it.payload }
        }
    }
}
