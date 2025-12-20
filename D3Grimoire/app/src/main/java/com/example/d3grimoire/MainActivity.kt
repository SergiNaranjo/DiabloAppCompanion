package com.example.d3grimoire

import API.AuthResponse
import API.BlizzardAuthInstance
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.d3grimoire.posts.news.NewsActivity
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

    private lateinit var analytics: FirebaseAnalytics
    private lateinit var database: DatabaseReference
    private lateinit var progressBar: ProgressBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.splash_screen)

        progressBar = findViewById(R.id.progressBar)
        progressBar.visibility = View.VISIBLE

        // Firebase
        analytics = Firebase.analytics
        analytics.logEvent("app_open", null)

        val databaseUrl =
            "https://appcompanion-eedc3-default-rtdb.europe-west1.firebasedatabase.app/"
        database = FirebaseDatabase.getInstance(databaseUrl)
            .getReference("messages")

        writeTestMessage()

        // Start loading
        fetchToken()
    }

    private fun writeTestMessage() {
        val dataId = database.push().key ?: return

        val messageData = mapOf(
            "user" to "Jose",
            "message" to "Hello World"
        )

        database.child(dataId)
            .setValue(messageData)
            .addOnSuccessListener {
                Log.d("FIREBASE", "Message written")
            }
            .addOnFailureListener {
                Log.e("FIREBASE", "Write failed", it)
            }
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
                if (!response.isSuccessful || response.body() == null) {
                    showError("Failed to authenticate")
                    return
                }

                TokenManager.token = response.body()!!.accessToken

                goToNewsScreen()
            }

            override fun onFailure(call: Call<AuthResponse>, t: Throwable) {
                showError("Network error")
                Log.e("TOKEN", "Failed to fetch token", t)
            }
        })
    }

    private fun goToNewsScreen() {
        progressBar.visibility = View.GONE
        startActivity(Intent(this, NewsActivity::class.java))
        finish()
    }

    private fun showError(message: String) {
        progressBar.visibility = View.GONE
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
    }
}
