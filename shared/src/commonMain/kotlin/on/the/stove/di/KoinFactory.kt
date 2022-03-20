package on.the.stove.di

import co.touchlab.kermit.Logger
import co.touchlab.kermit.StaticConfig
import io.ktor.client.*
import on.the.stove.database.AppDatabaseRepository
import on.the.stove.database.AppDatabaseRepositoryImpl
import on.the.stove.services.network.*
import org.koin.core.context.startKoin
import org.koin.core.module.Module
import org.koin.dsl.KoinAppDeclaration
import org.koin.dsl.bind
import org.koin.dsl.module

fun initKoin(appDeclaration: KoinAppDeclaration = {}) =
    startKoin {
        appDeclaration()
        modules(platformModule, commonModule)
    }

fun initKoin() = initKoin {
    val log = Logger(config = StaticConfig(), tag = "KOIN_")
    log.d("initKoin ios")
}

val commonModule = module {
    single { AppDatabaseRepositoryImpl() } bind AppDatabaseRepository::class

    // region Network
    single { NetworkService().httpClient } bind HttpClient::class
    single(HostQualifier, definition = { HostQualifier.value })
    // endregion Network

    // region Api
    single { RecipeApiImpl() } bind RecipesApi::class
    single { CategoriesApiImpl() } bind CategoriesApi::class
    // endregion Api
}
expect val platformModule: Module