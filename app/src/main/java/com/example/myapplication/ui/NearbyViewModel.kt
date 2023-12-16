package com.example.myapplication.ui;

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.data.NearbyVenuesRepository
import com.example.myapplication.model.Location
import com.example.myapplication.model.Venue
import com.example.myapplication.model.VenueDB
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

class NearbyViewModel @Inject constructor(private val nearbyVenuesRepository: NearbyVenuesRepository) : ViewModel() {

    private val locationLiveData = MutableLiveData<Location>()
    private val nearbyVenues = MutableLiveData<MutableList<Venue>?>()
    private val nearbyVenuesDB = nearbyVenuesRepository.venuesDb
    private var range = 1
    private var page = 1

    fun fetchNearbyLocations(page: Int, latitude: Double, longitude: Double, range: Int, query: String? = "") {
        this.range = range
        this.page = page
        viewModelScope.launch(Dispatchers.IO) {
            val fetchedVenues: List<Venue>? =
                nearbyVenuesRepository.fetchNearbyLocations(page, latitude, longitude, range, query)
            if (nearbyVenues.value == null) {
                nearbyVenues.value = fetchedVenues as MutableList<Venue>?
            } else {
                if (fetchedVenues != null) {
                    nearbyVenues.value?.addAll(fetchedVenues)
                }
            }
        }
    }

    fun updateLocation(latitude: Double, longitude: Double) {
        locationLiveData.value = Location(latitude, longitude)
    }

    fun getLocationLiveData(): LiveData<Location> = locationLiveData
    fun getNearbyVenuesLiveData(): LiveData<MutableList<Venue>?> = nearbyVenues
    fun getNearbyVenuesDBLiveData(): LiveData<List<VenueDB>> = nearbyVenuesDB
    fun getRange(): Int = range
    fun getPage(): Int = page
}
