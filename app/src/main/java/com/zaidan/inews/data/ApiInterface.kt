package com.zaidan.inews.data

import com.zaidan.inews.data.response.MainResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

interface ApiInterface {

    @GET("top-headlines")
    suspend fun getNews(@Query("category") category: String, @Header("x-api-key") token: String = "a637e20164c04fe984df97d03ff69f2a", @Query("country") country: String = "id"): Response<MainResponse>
}