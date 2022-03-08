package on.the.stove.services.network

import io.ktor.client.*
import io.ktor.client.features.*
import io.ktor.client.features.get
import io.ktor.client.features.json.*
import io.ktor.client.features.json.serializer.*
import io.ktor.client.features.logging.*
import io.ktor.client.request.*
import io.ktor.http.*
import on.the.stove.dto.Recipe
import on.the.stove.services.responseModels.Response

internal object NetworkService {

    val httpClient = HttpClient {
        install(JsonFeature) {
            val json = kotlinx.serialization.json.Json { ignoreUnknownKeys = true }
            serializer = KotlinxSerializer(json)
            acceptContentTypes = acceptContentTypes + ContentType.Any
        }
        install(HttpTimeout) {
            requestTimeoutMillis = 10_000L
        }
        install(Logging) {
            logger = Logger.DEFAULT
            level = LogLevel.HEADERS
        }
    }
}

