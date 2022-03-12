package on.the.stove.services.network

import io.ktor.client.*
import io.ktor.client.request.*
import kotlinx.coroutines.withContext
import on.the.stove.dispatchers.ktorDispatcher
import on.the.stove.dto.Category
import on.the.stove.dto.RecipeDetails
import on.the.stove.dto.Recipe
import on.the.stove.services.responseModels.Response
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

internal interface RecipesApi {

    suspend fun getRecipesList(page: Int?, category: Category): Result<List<Recipe>>

    suspend fun getDetailedRecipe(id: String): Result<RecipeDetails>
}

internal class RecipeApiImpl : RecipesApi, KoinComponent {

    private val hostUrl: String by inject(HostQualifier)
    private val client: HttpClient by inject()

    override suspend fun getRecipesList(page: Int?, category: Category): Result<List<Recipe>> {
        return withContext(ktorDispatcher) {
            runCatching<Response<List<Recipe>>> {
                client.get("$hostUrl/recipes") {
                    parameter("page", page)
                    parameter("category", category.id)
                }
            }.map { it.payload }
        }
    }

    override suspend fun getDetailedRecipe(id: String): Result<RecipeDetails> {
        return withContext(ktorDispatcher) {
            runCatching<Response<RecipeDetails>> {
                client.get("$hostUrl/recipe/show") {
                    parameter("id", id)
                }
            }.map { it.payload }
        }
    }
}
