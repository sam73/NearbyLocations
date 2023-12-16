package com.example.myapplication.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class VenueDB(
    @PrimaryKey val locationId: String,
    val name: String,
    val address: String,
    val extendedAddress: String
)