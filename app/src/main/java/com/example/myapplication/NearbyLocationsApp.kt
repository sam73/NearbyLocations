package com.example.myapplication

import android.app.Application
import com.example.myapplication.di.DaggerNearbyVenueComponent
import com.example.myapplication.di.NearbyVenueComponent
import com.example.myapplication.di.NearbyVenueModule

class NearbyLocationsApp: Application() {

    lateinit var myComponent: NearbyVenueComponent

    override fun onCreate() {
        super.onCreate()
        myComponent = DaggerNearbyVenueComponent.builder()
            .nearbyVenueModule(NearbyVenueModule(this))
            .build()
    }
}