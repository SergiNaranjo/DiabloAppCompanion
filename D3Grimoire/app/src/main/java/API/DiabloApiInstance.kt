package API

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import repository.TokenManager

object DiabloApiInstance {

    private const val BASE_URL = "https://eu.api.blizzard.com/"

    private val gson: Gson = GsonBuilder()
        .setLenient()
        .create()

    private val client: OkHttpClient = OkHttpClient.Builder()
        .addInterceptor { chain ->
            val token: String? = TokenManager.token

            val request = chain.request().newBuilder()
                .addHeader("Authorization", "Bearer $token")
                .build()

            chain.proceed(request)
        }
        .build()

    val api: DiabloApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
            .create(DiabloApiService::class.java)
    }
}
