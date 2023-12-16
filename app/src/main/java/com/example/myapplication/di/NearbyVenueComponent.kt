package com.example.myapplication.di

import com.example.myapplication.ui.NearbyMainActivity
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [NearbyVenueModule::class])
interface NearbyVenueComponent {
    fun inject(activity: NearbyMainActivity)
}
