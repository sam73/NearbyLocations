package com.example.myapplication.model

import com.google.gson.annotations.SerializedName

data class Venue(
    @SerializedName("id")
    val id: String,
    @SerializedName("name")
    val name: String,
    @SerializedName("address")
    val address: String,
    @SerializedName("extended")
    val extendedAddress: String
)