package com.practicum.playlistmaker.recycleView


import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface APIItunes {
    @GET("/search?entity=song")
    fun search(@Query("term") text: String): Call<ItunesResponse>
}