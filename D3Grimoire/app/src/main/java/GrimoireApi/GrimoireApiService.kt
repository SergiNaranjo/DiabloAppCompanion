package GrimoireApi

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface GrimoireApiService {

    @GET("v1/public/characters")
    fun getCommunity(
        @Query("apikey") apiKey: String,
        @Query("ts") timeStamp: String,
        @Query("hash") hash: String,
        @Query("limit") limit: Int = 20,
    ): Call<GrimoireResponse>
}