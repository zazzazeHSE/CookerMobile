package on.the.stove.services.network

import io.ktor.client.*
import io.ktor.client.request.*
import kotlinx.coroutines.withContext
import on.the.stove.dispatchers.ktorDispatcher
import on.the.stove.dto.Category
import on.the.stove.services.responseModels.Response
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

private typealias Categories = List<Category>

internal interface CategoriesApi {

    suspend fun getCategories(): Result<Categories>
}

internal class CategoriesApiImpl : CategoriesApi, KoinComponent {

    private val hostUrl: String by inject(HostQualifier)
    private val client: HttpClient by inject()

    override suspend fun getCategories(): Result<Categories> {
        return withContext(ktorDispatcher) {
            runCatching<Response<Categories>> {
                client.get("$hostUrl/recipe/categories")
            }.map { it.payload }
        }
    }
}
