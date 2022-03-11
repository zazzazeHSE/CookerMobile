package on.the.stove.di

import co.touchlab.kermit.Logger
import co.touchlab.kermit.StaticConfig
import org.koin.core.context.startKoin
import org.koin.core.module.Module
import org.koin.dsl.KoinAppDeclaration
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

}
expect val platformModule: Module