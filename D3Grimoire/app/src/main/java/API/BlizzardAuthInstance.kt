package API

import okhttp3.Credentials
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object BlizzardAuthInstance {

    private const val BASE_URL = "https://oauth.battle.net/"

    fun create(clientId: String, clientSecret: String): BlizzardAuthApi {
        val auth = Credentials.basic(clientId, clientSecret)

        val client = OkHttpClient.Builder()
            .addInterceptor { chain ->
                val request = chain.request().newBuilder()
                    .addHeader("Authorization", auth)
                    .build()
                chain.proceed(request)
            }
            .build()

        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(BlizzardAuthApi::class.java)
    }
}