package ru.netology.mapmarkers.application

import android.app.Application
import com.yandex.mapkit.MapKitFactory
import ru.netology.mapmarkers.BuildConfig



class App : Application() {

    override fun onCreate() {
        super.onCreate()
        MapKitFactory.setApiKey(BuildConfig.MAPS_API_KEY)
    }
}