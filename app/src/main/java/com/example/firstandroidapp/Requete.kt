package com.example.firstandroidapp

import android.os.Parcelable
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import kotlinx.coroutines.Deferred
import kotlinx.parcelize.Parcelize
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

data class ServerResponse(
    val response: Response,
    val error: String?,
)

@Parcelize
data class Response(
    val name: String,
    val picture: String,
    val altName: String,
    val barcode: String,
    val ingredient: List<String>,
): Parcelable

interface FoodAPI {
    @GET("/getProduct")
    fun getFood(
        @Query("barcode") barecode: String
    ): Deferred<ServerResponse>
}

object NetworkManager2 {

    private val api = Retrofit.Builder()
        .baseUrl("https://api.formation-android.fr/")
        .addConverterFactory(GsonConverterFactory.create())
        .addCallAdapterFactory(CoroutineCallAdapterFactory())
        .build()
        .create(FoodAPI::class.java)

    fun getFood(barecode: String): Deferred<ServerResponse> {
        return api.getFood(barecode)
    }

}