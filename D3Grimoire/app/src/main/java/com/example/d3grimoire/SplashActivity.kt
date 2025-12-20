package com.example.d3grimoire

import API.AuthResponse
import API.BlizzardAuthInstance
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import com.example.d3grimoire.BuildConfig
import repository.TokenManager

class SplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.splash_screen)

        fetchToken()
    }

    private fun fetchToken() {
        val api = BlizzardAuthInstance.create(
            clientId = BuildConfig.BLIZZARD_CLIENT_ID,
            clientSecret = BuildConfig.BLIZZARD_CLIENT_SECRET
        )

        api.getAccessToken().enqueue(object : Callback<AuthResponse> {
            override fun onResponse(
                call: Call<AuthResponse>,
                response: Response<AuthResponse>
            ) {
                TokenManager.token = response.body()?.accessToken

                startActivity(Intent(this@SplashActivity, MainActivity::class.java))
                finish()
            }

            override fun onFailure(call: Call<AuthResponse>, t: Throwable) {
                Log.e("TOKEN", "Failed to fetch token", t)
            }
        })
    }
}
