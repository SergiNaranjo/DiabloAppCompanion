package GrimoireApi

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object GrimoireApiInstance {
    private const val BASE_URL = "//TODO"
    //ADD API's for community API and gameData API

    val apiService: GrimoireApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(GrimoireApiService::class.java)
    }
}