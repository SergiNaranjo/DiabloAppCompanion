package com.example.d3grimoire

import API.AuthResponse
import API.BlizzardAuthInstance
import repository.TokenManager
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.splash_screen)

        fetchToken()
    }

    private fun fetchToken() {
        val api = BlizzardAuthInstance.create(
            clientId = "YOUR_CLIENT_ID",
            clientSecret = "YOUR_CLIENT_SECRET"
        )

        api.getAccessToken().enqueue(object : Callback<AuthResponse> {

            override fun onResponse(
                call: Call<AuthResponse>,
                response: Response<AuthResponse>
            ) {
                if (response.isSuccessful && response.body() != null) {

                    //Save token
                    TokenManager.token = response.body()!!.accessToken
                    Log.d("TOKEN", "Token received")

                    //Navigate to NewsActivity
                    startActivity(
                        Intent(this@SplashActivity, NewsActivity::class.java)
                    )
                    finish()

                } else {
                    Log.e("TOKEN", "Response error: ${response.code()}")
                }
            }

            override fun onFailure(call: Call<AuthResponse>, t: Throwable) {
                Log.e("TOKEN", "Token request failed", t)
            }
        })
    }
}
