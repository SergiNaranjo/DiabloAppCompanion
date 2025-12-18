package com.example.d3grimoire

import API.model.SeasonResponse
import API.repository.DiabloRepository
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import repository.TokenManager
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class NewsActivity : AppCompatActivity() {

    private val repository = DiabloRepository()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.news_screen)

        loadSeasons()
    }

    private fun loadSeasons() {
        val token = TokenManager.token ?: return

        repository.getSeasons(token).enqueue(object : Callback<SeasonResponse> {
            override fun onResponse(call: Call<SeasonResponse>, response: Response<SeasonResponse>) {
                val seasons = response.body()?.season
                // TODO: show in RecyclerView
            }

            override fun onFailure(call: Call<SeasonResponse>, t: Throwable) {
                Toast.makeText(this@NewsActivity, "Error", Toast.LENGTH_SHORT).show()
            }
        })
    }
}
