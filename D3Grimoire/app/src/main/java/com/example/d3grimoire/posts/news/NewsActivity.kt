package com.example.d3grimoire.posts.news

import API.model.ClassDetailResponse
import API.model.ClassesResponse
import API.model.ItemResponse
import API.model.SeasonResponse
import API.repository.DiabloRepository
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.d3grimoire.R
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class NewsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.news_screen)

        loadSeasons()
    }

    private fun loadSeasons() {
        DiabloRepository.getSeasons()
            .enqueue(object : Callback<SeasonResponse> {

                override fun onResponse(
                    call: Call<SeasonResponse>,
                    response: Response<SeasonResponse>
                ) {
                    if (response.isSuccessful) {
                        val seasons = response.body()?.season
                        seasons?.forEach {
                            Log.d("SEASON", "Season ID: ${it.id}")
                        }
                    }
                }

                override fun onFailure(call: Call<SeasonResponse>, t: Throwable) {
                    Toast.makeText(this@NewsActivity, "API Error", Toast.LENGTH_SHORT).show()
                }
            })
    }

    private fun loadClasses() {
        DiabloRepository.getClasses()
            .enqueue(object : Callback<ClassesResponse> {
                override fun onResponse(call: Call<ClassesResponse>, response: Response<ClassesResponse>) {
                    if (response.isSuccessful) {
                        val classes = response.body()?.classes
                        classes?.forEach {
                            Log.d("CLASS", "Class: ${it.name} (${it.slug})")
                        }
                    }
                }

                override fun onFailure(call: Call<ClassesResponse>, t: Throwable) {
                    //TODO REPLACE NEWS ACTIVITY WITH THE ACTUAL ACTIVITY THIS FUNCTION WILL BE IN
                    Toast.makeText(this@NewsActivity, "API Error", Toast.LENGTH_SHORT).show()
                }
            })
    }

    private fun loadClassDetail(slug: String) {
        DiabloRepository.getClassDetail(slug)
            .enqueue(object : Callback<ClassDetailResponse> {
                override fun onResponse(call: Call<ClassDetailResponse>, response: Response<ClassDetailResponse>) {
                    if (response.isSuccessful) {
                        val skills = response.body()?.skills
                        skills?.active?.forEach {
                            Log.d("SKILL", "Active: ${it.name} - ${it.description}")
                        }
                        skills?.passive?.forEach {
                            Log.d("SKILL", "Passive: ${it.name} - ${it.description}")
                        }
                    }
                }

                override fun onFailure(call: Call<ClassDetailResponse>, t: Throwable) {
                    //TODO REPLACE NEWS ACTIVITY WITH THE ACTUAL ACTIVITY THIS FUNCTION WILL BE IN
                    Toast.makeText(this@NewsActivity, "API Error", Toast.LENGTH_SHORT).show()
                }
            })
    }

    private fun loadItem(itemSlug: String) {
        DiabloRepository.getItem(itemSlug)
            .enqueue(object : Callback<ItemResponse> {
                override fun onResponse(call: Call<ItemResponse>, response: Response<ItemResponse>) {
                    if (response.isSuccessful) {
                        val item = response.body()
                        Log.d("ITEM", "Name: ${item?.name}, Damage: ${item?.damage}, APS: ${item?.attacksPerSecond}")
                    }
                }

                override fun onFailure(call: Call<ItemResponse>, t: Throwable) {
                    //TODO REPLACE NEWS ACTIVITY WITH THE ACTUAL ACTIVITY THIS FUNCTION WILL BE IN
                    Toast.makeText(this@NewsActivity, "API Error", Toast.LENGTH_SHORT).show()
                }
            })
    }
}
