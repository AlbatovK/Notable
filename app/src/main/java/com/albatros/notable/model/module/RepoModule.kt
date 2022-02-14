package com.albatros.notable.model.module

import com.albatros.notable.model.repo.MainRepository
import org.koin.dsl.module

val repoModule = module {
    single { MainRepository(get(), get()) }
}