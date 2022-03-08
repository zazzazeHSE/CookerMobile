package on.the.stove.services.requestBuilders

import io.ktor.http.*

data class RecipesRequestBuilder(
    val category: Int?,
    val page: Int?
): APIRequestBuilder() {
    companion object {
        const val CATEGORY_PARAM = "category"
        const val PAGE_PARAM = "page"
        const val PATH = "recipes"
    }

    override fun prepareURL(): String {
        val url = super.prepareURL()
        val builder = URLBuilder(url)
        builder.path(PATH)
        if (category != null) {
            builder.parameters.append(CATEGORY_PARAM, category.toString())
        }
        if (page != null) {
            builder.parameters.append(PAGE_PARAM, page.toString())
        }

        return builder.buildString()
    }
}