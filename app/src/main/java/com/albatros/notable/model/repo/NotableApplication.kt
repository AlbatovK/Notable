package com.albatros.notable.model.repo

import android.app.Application
import androidx.camera.camera2.Camera2Config
import androidx.camera.core.CameraXConfig
import com.albatros.notable.model.module.appModule
import com.albatros.notable.model.module.repoModule
import com.albatros.notable.model.module.viewModelModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

@Suppress("unused")
class NotableApplication : Application(), CameraXConfig.Provider {

    private val modules = listOf(appModule, repoModule, viewModelModule)

    override fun getCameraXConfig(): CameraXConfig = Camera2Config.defaultConfig()

    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@NotableApplication)
            modules(modules)
        }
    }
}