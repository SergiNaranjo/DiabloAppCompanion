package API

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object DiabloApiInstance {
    private const val BASE_URL = "https://eu.api.blizzard.com/"

    val api: DiabloApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(DiabloApiService::class.java)
    }
}