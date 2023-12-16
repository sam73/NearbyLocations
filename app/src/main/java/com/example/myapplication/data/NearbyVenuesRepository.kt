package com.example.myapplication.data

import androidx.lifecycle.LiveData
import com.example.myapplication.db.NearbyVenueDao
import com.example.myapplication.model.Venue
import com.example.myapplication.model.VenueDB
import com.example.myapplication.retrofit.NearbyAPIService
import javax.inject.Inject

class NearbyVenuesRepository @Inject constructor(
    private val localDataSource: NearbyVenueDao,
    private val remoteDataSource: NearbyAPIService
    ) {

    val venuesDb: LiveData<List<VenueDB>> = localDataSource.getAllData()

    suspend fun fetchNearbyLocations(
        page: Int,
        latitude: Double,
        longitude: Double,
        range: Int,
        query: String? = ""
    ): List<Venue>? {
        // Fetch data from the network
        val list = remoteDataSource.getVenues(
            page = page,
            latitude = latitude, longitude = longitude, range = "${range}mi", query = query
        ).body()
        // Update DB
        if (list != null) {
            localDataSource.insertAll(list.map {
                VenueDB(it.id,it.name, it.address, it.extendedAddress)
            })
        }
        return list
    }
}