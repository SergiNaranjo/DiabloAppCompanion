package API

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object BlizzardAuthInstance {
    private const val BASE_URL = "https://oauth.battle.net/" //https://eu.api.blizzard.com

    val api: BlizzardAuthApi by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(BlizzardAuthApi::class.java)
    }
}
