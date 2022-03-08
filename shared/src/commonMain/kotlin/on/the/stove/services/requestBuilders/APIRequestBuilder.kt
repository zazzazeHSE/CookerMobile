package on.the.stove.services.requestBuilders

abstract class APIRequestBuilder {
    companion object {
        const val baseUrl = "http://195.2.80.162:80"
    }

    open fun prepareURL(): String {
        return baseUrl
    }
}