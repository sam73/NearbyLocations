package com.example.myapplication.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.myapplication.model.VenueDB

@Database(entities = [VenueDB::class], version = 1)
abstract class NearbyVenueDatabase : RoomDatabase() {
    abstract fun nearbyVenueDao(): NearbyVenueDao
}
