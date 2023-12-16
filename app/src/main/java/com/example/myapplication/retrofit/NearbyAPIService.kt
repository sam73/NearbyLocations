package com.example.myapplication.retrofit

import com.example.myapplication.model.Venue
import com.example.myapplication.utils.Constants
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface NearbyAPIService {

    @GET("2/venues")
    fun getVenues(
        @Query("client_id") clientId: String = Constants.CLIENT_ID,
        @Query("per_page") perPage: Int = Constants.PAGE_ITEM_COUNT,
        @Query("page") page: Int,
        @Query("lat") latitude: Double,
        @Query("lon") longitude: Double,
        @Query("range") range: String = "${Constants.DEFAULT_DISTANCE}mi",
        @Query("q") query: String? = ""
    ): Response<List<Venue>>
}