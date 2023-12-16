package com.example.myapplication.ui

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.SeekBar
import android.widget.Toast
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.NearbyLocationsApp
import com.example.myapplication.R
import com.example.myapplication.databinding.NearbyActivityMainBinding
import com.example.myapplication.model.Venue
import com.example.myapplication.utils.Constants
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import javax.inject.Inject

class NearbyMainActivity : AppCompatActivity() {

    companion object {
        private const val MY_PERMISSIONS_REQUEST_LOCATION = 123
    }

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private val nearbyViewModel by viewModels<NearbyViewModel> { viewModelFactory }
    private lateinit var binding: NearbyActivityMainBinding
    private lateinit var recyclerView: RecyclerView

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        (application as NearbyLocationsApp).myComponent.inject(this)
        binding = DataBindingUtil.setContentView(this, R.layout.nearby_activity_main)
        recyclerView = binding.nearbyLocations
        recyclerView.layoutManager = LinearLayoutManager(this)
        setupSeekbar()
        setupSearchBar()
        getLocation()
        addObservers()
    }

    private fun addObservers() {
        nearbyViewModel.getNearbyVenuesLiveData().observe(this) {
            recyclerView.adapter = it?.let { it1 -> VenueAdapter(it1) }
        }

        nearbyViewModel.getLocationLiveData().observe(this) {
            nearbyViewModel.fetchNearbyLocations(1, it.latitude, it.longitude, Constants.DEFAULT_DISTANCE)
        }
        
        nearbyViewModel.getNearbyVenuesDBLiveData().observe(this) { it ->
            recyclerView.adapter = it?.let { it1 -> VenueAdapter(it1.map { 
                Venue(it.locationId, it.name, it.address, it.extendedAddress)
            }) }
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun setupSeekbar() {
        binding.slider.progress = Constants.DEFAULT_DISTANCE
        binding.slider.min = Constants.MIN_DISTANCE
        binding.slider.max = Constants.MAX_DISTANCE

        binding.slider.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                nearbyViewModel.getLocationLiveData().value?.let {
                    nearbyViewModel.fetchNearbyLocations(
                        1,
                        it.latitude,
                        it.longitude,
                        progress
                    )
                }
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
                TODO("Not yet implemented")
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
                TODO("Not yet implemented")
            }
        })
    }

    private fun setupSearchBar() {
        binding.searchBar.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            }

            override fun afterTextChanged(s: Editable?) {
                nearbyViewModel.getLocationLiveData().value?.let {
                    nearbyViewModel.fetchNearbyLocations(
                        nearbyViewModel.getPage(),
                        it.latitude,
                        it.longitude,
                        nearbyViewModel.getRange()
                    )
                }
            }
        })
    }

    private fun getLocation() {
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            // Request the permission
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                MY_PERMISSIONS_REQUEST_LOCATION
            )
        }

        val fusedLocationClient: FusedLocationProviderClient =
            LocationServices.getFusedLocationProviderClient(this)

        // Check if the location permission is granted
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            // Request the last known location
            fusedLocationClient.lastLocation
                .addOnSuccessListener(this) { location ->
                    if (location != null) {
                        nearbyViewModel.updateLocation(location.latitude, location.longitude)
                    }
                }
                .addOnFailureListener(this) { e ->
                    // Handle failure
                    Log.e("NearbyMainActivity", "Error getting location", e)
                }
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (requestCode == MY_PERMISSIONS_REQUEST_LOCATION) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission granted, proceed to fetch the location
                getLocation()
            } else {
                Toast.makeText(this, Constants.LOCATION_NOT_AVAILABLE_TEXT, Toast.LENGTH_SHORT).show()
            }
        }
    }
}