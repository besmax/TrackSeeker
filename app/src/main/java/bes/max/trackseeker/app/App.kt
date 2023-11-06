package bes.max.trackseeker.app

import android.app.Application
import bes.max.trackseeker.di.dataModule
import bes.max.trackseeker.di.domainModule
import bes.max.trackseeker.di.repositoryModule
import bes.max.trackseeker.di.viewModelModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin


class App : Application() {

    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@App)
            modules(dataModule, domainModule, viewModelModule, repositoryModule)
        }

//        val settingsDao: SettingsDao = getKoin().get()
//        kotlinx.coroutines.MainScope().launch {
//            Log.d(
//                "AppClass",
//                "isNightModeActiveDefault returns: ${settingsDao.isNightModeActive()}"
//            )
//            settingsDao.isNightModeActive().asLiveData().observeForever { switchTheme(it) }
//        }

    }

//    private fun switchTheme(darkThemeEnabled: Boolean) {
//        AppCompatDelegate.setDefaultNightMode(
//            if (darkThemeEnabled) AppCompatDelegate.MODE_NIGHT_YES
//            else AppCompatDelegate.MODE_NIGHT_NO
//
//        )
//
//    }
}