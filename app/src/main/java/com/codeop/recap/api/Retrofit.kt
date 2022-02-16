package com.codeop.recap.api

import com.codeop.recap.data.ComicResponse
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path

object Retrofit {
private val baseUrl = "https://xkcd.com/"
    val retrofit = Retrofit.Builder()
        .baseUrl(baseUrl)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val xkcdService = retrofit.create(API::class.java)
}

interface API {
    @GET("info.0.json")
    fun getNewestComic() : Call<ComicResponse>

    @GET("{number}/info.0.json")
    fun getSpecificComic(@Path("number") comicNumber: Int): Call<ComicResponse>
}