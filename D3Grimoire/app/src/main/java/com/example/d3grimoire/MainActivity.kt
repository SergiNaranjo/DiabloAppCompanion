package com.example.d3grimoire

import API.AuthResponse
import API.BlizzardAuthApi
import API.BlizzardAuthInstance
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.d3grimoire.signin.UserHandler
import com.google.firebase.Firebase
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.analytics
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import repository.TokenManager
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {
    private lateinit var progressBar: ProgressBar

    companion object {
        private const val TAG: String = "SPLASH_API";
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)

        UserHandler.Init(this);
        FirebaseHandler.Init(this);

        progressBar = findViewById(R.id.progressBar)
        progressBar.visibility = View.VISIBLE

        // Start loading
        fetchToken()
    }

    private fun fetchToken() {
        val api: BlizzardAuthApi = BlizzardAuthInstance.create(
            clientId = BuildConfig.BLIZZARD_CLIENT_ID,
            clientSecret = BuildConfig.BLIZZARD_CLIENT_SECRET
        )

        api.getAccessToken().enqueue(object : Callback<AuthResponse> {

            override fun onResponse(
                call: Call<AuthResponse>,
                response: Response<AuthResponse>
            ) {
                Log.d(TAG, "Token response received: code=${response.code()}")

                if (!response.isSuccessful || response.body() == null) {
                    Log.e(TAG, "Token request failed: ${response.errorBody()?.string()}")
                    showError("Failed to authenticate")
                    return
                }

                val token: String = response.body()!!.accessToken
                TokenManager.token = token

                goToNewsScreen()
            }

            override fun onFailure(call: Call<AuthResponse>, t: Throwable) {
                Log.e(TAG, "Token request failed due to network error", t)
                showError("Network error")
            }
        })
    }

    private fun goToNewsScreen() {
        progressBar.visibility = View.GONE
        startActivity(Intent(this, NavBarActivity::class.java))
        finish()
    }

    private fun showError(message: String) {
        progressBar.visibility = View.GONE
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
    }
}
