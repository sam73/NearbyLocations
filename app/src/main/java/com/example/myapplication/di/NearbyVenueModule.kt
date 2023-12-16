package com.example.myapplication.di

import androidx.lifecycle.ViewModelProvider
import androidx.room.Room.databaseBuilder
import com.example.myapplication.NearbyLocationsApp
import com.example.myapplication.db.NearbyVenueDao
import com.example.myapplication.db.NearbyVenueDatabase
import com.example.myapplication.retrofit.NearbyAPIService
import com.example.myapplication.ui.NearbyViewModel
import com.example.myapplication.utils.Constants
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
class NearbyVenueModule(private val application: NearbyLocationsApp) {

    @Provides
    @Singleton
    fun provideYourDatabase(): NearbyVenueDatabase {
        return databaseBuilder(application, NearbyVenueDatabase::class.java, "nearbyVenueDB")
            .build()
    }

    @Provides
    @Singleton
    fun provideVenueDao(database: NearbyVenueDatabase): NearbyVenueDao {
        return database.nearbyVenueDao()
    }

    @Provides
    @Singleton
    fun provideRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl(Constants.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Singleton
    @Provides
    fun provideApiService(retrofit: Retrofit): NearbyAPIService {
        return retrofit.create(NearbyAPIService::class.java)
    }

    @Singleton
    @Provides
    fun provideViewModelFactory(viewModel: NearbyViewModel): ViewModelProvider.Factory {
        return ViewModelFactory(viewModel)
    }
}