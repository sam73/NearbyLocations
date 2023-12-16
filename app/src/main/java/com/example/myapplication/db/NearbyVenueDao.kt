package com.example.myapplication.db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.myapplication.model.VenueDB

@Dao
interface NearbyVenueDao {

    @Query("SELECT * FROM VenueDB")
    fun getAllData(): LiveData<List<VenueDB>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(dataList: List<VenueDB>)
}
