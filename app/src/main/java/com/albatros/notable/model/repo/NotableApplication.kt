package com.albatros.notable.model.repo

import android.app.Application
import com.albatros.notable.model.module.appModule
import com.albatros.notable.model.module.repoModule
import com.albatros.notable.model.module.viewModelModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

@Suppress("unused")
class NotableApplication : Application() {

    private val modules = listOf(appModule, repoModule, viewModelModule)

    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@NotableApplication)
            modules(modules)
        }
    }
}