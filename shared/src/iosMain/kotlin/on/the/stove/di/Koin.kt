package on.the.stove.di

import on.the.stove.database.DatabaseDriverFactory
import org.koin.core.module.Module
import org.koin.dsl.module

actual val platformModule: Module = module(createdAtStart = true) {
    single {
        DatabaseDriverFactory()
    }
}