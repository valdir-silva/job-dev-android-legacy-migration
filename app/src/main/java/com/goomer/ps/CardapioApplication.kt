package com.goomer.ps

import android.app.Application
import com.goomer.ps.di.appModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level

class CardapioApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidLogger(
                if (BuildConfig.DEBUG) Level.DEBUG else Level.ERROR
            )

            androidContext(this@CardapioApplication)

            modules(appModule)
        }
    }
}

