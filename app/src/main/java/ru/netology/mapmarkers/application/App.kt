package ru.netology.mapmarkers.application

import android.app.Application
import com.yandex.mapkit.MapKitFactory

private val MAPS_API_KEY = "843679b6-ddc8-4f6a-a4ca-40c7ea099ce8"
class App : Application() {

    override fun onCreate() {
        super.onCreate()
        MapKitFactory.setApiKey(MAPS_API_KEY)
    }
}