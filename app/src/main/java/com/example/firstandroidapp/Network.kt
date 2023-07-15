package com.example.firstandroidapp

import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import kotlinx.coroutines.Deferred
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

data class Age(
    val name: String,
    val age: Int,
)

interface AgifyAPI {
    @GET("/")
    fun getAge(
        @Query("name") name: String,
        @Query("country_id") countryId: String,
    ): Deferred<Age>
}

object NetworkManager {

    private val api = Retrofit.Builder()
        .baseUrl("https://api.agify.io/")
        .addConverterFactory(GsonConverterFactory.create())
        .addCallAdapterFactory(CoroutineCallAdapterFactory())
        .build()
        .create(AgifyAPI::class.java)

    fun getAverageAge(name: String): Deferred<Age> {
        return api.getAge(name, "FR")
    }

}