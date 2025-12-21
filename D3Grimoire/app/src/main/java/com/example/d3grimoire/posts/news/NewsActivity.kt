package com.example.d3grimoire.posts.news

import API.DiabloApiInstance
import API.model.ClassDetailResponse
import API.model.ClassesResponse
import API.model.ItemResponse
import API.model.SeasonResponse
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.d3grimoire.R
import repository.TokenManager
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class NewsActivity : AppCompatActivity() {

    companion object {
        private const val TAG_SEASON = "SEASON_API"
        private const val TAG_CLASS = "CLASS_API"
        private const val TAG_SKILL = "SKILL_API"
        private const val TAG_ITEM = "ITEM_API"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.news_screen)

        Log.d(TAG_SEASON, "NewsActivity started, loading API data")

        loadSeasons()
        loadClasses()
    }

    private fun loadSeasons() {
        Log.d(TAG_SEASON, "Fetching seasons from API")

        TokenManager.token?.let { token ->
            DiabloApiInstance.api.getSeasons(token = token)
                .enqueue(object : Callback<SeasonResponse> {
                    override fun onResponse(call: Call<SeasonResponse>, response: Response<SeasonResponse>) {
                        if (response.isSuccessful) {
                            response.body()?.season?.forEach {
                                Log.d(TAG_SEASON, "Season ID: ${it.id}")
                            }
                        } else {
                            Log.e(TAG_SEASON, "Seasons API failed with code: ${response.code()}")
                        }
                    }

                    override fun onFailure(call: Call<SeasonResponse>, t: Throwable) {
                        Log.e(TAG_SEASON, "Seasons API request failed", t)
                    }
                })
        } ?: run {
            Log.e(TAG_SEASON, "Token is null, cannot fetch seasons")
        }
    }

    private fun loadClasses() {
        Log.d(TAG_CLASS, "Fetching classes from API")

        TokenManager.token?.let { token ->
            DiabloApiInstance.api.getClasses(token = token)
                .enqueue(object : Callback<ClassesResponse> {
                    override fun onResponse(call: Call<ClassesResponse>, response: Response<ClassesResponse>) {
                        if (response.isSuccessful) {
                            val classes = response.body()?.classes
                            Log.d(TAG_CLASS, "Classes API success, ${classes?.size ?: 0} items")
                            classes?.forEach {
                                Log.d(TAG_CLASS, "Class: ${it.name} (${it.slug})")
                                // Example: load details for each class
                                loadClassDetail(it.slug)
                            }
                        } else {
                            Log.e(TAG_CLASS, "Classes API failed with code: ${response.code()}")
                            Toast.makeText(this@NewsActivity, "Failed to load classes", Toast.LENGTH_SHORT).show()
                        }
                    }

                    override fun onFailure(call: Call<ClassesResponse>, t: Throwable) {
                        Log.e(TAG_CLASS, "Classes API call failed", t)
                        Toast.makeText(this@NewsActivity, "API Error", Toast.LENGTH_SHORT).show()
                    }
                })
        } ?: run {
            Log.e(TAG_CLASS, "Token is null, cannot fetch classes")
        }
    }

    private fun loadClassDetail(slug: String) {
        Log.d(TAG_SKILL, "Fetching class details for: $slug")

        TokenManager.token?.let { token ->
            DiabloApiInstance.api.getClassDetail(slug = slug, token = token)
                .enqueue(object : Callback<ClassDetailResponse> {
                    override fun onResponse(call: Call<ClassDetailResponse>, response: Response<ClassDetailResponse>) {
                        if (response.isSuccessful) {
                            val skills = response.body()?.skills
                            Log.d(TAG_SKILL, "ClassDetail API success for $slug")

                            skills?.active?.forEach {
                                Log.d(TAG_SKILL, "Active: ${it.name} - ${it.description}")
                            }
                            skills?.passive?.forEach {
                                Log.d(TAG_SKILL, "Passive: ${it.name} - ${it.description}")
                            }
                        } else {
                            Log.e(TAG_SKILL, "ClassDetail API failed for $slug with code: ${response.code()}")
                            Toast.makeText(this@NewsActivity, "Failed to load class details", Toast.LENGTH_SHORT).show()
                        }
                    }

                    override fun onFailure(call: Call<ClassDetailResponse>, t: Throwable) {
                        Log.e(TAG_SKILL, "ClassDetail API call failed for $slug", t)
                        Toast.makeText(this@NewsActivity, "API Error", Toast.LENGTH_SHORT).show()
                    }
                })
        } ?: run {
            Log.e(TAG_SKILL, "Token is null, cannot fetch class details for $slug")
        }
    }

    private fun loadItem(slug: String) {
        Log.d(TAG_ITEM, "Fetching item details for: $slug")

        TokenManager.token?.let { token ->
            DiabloApiInstance.api.getItem(slug = slug, token = token)
                .enqueue(object : Callback<ItemResponse> {
                    override fun onResponse(call: Call<ItemResponse>, response: Response<ItemResponse>) {
                        if (response.isSuccessful) {
                            val item = response.body()
                            Log.d(TAG_ITEM, "Item API success: Name=${item?.name}, Damage=${item?.damage}, APS=${item?.attacksPerSecond}")
                        } else {
                            Log.e(TAG_ITEM, "Item API failed for $slug with code: ${response.code()}")
                            Toast.makeText(this@NewsActivity, "Failed to load item", Toast.LENGTH_SHORT).show()
                        }
                    }

                    override fun onFailure(call: Call<ItemResponse>, t: Throwable) {
                        Log.e(TAG_ITEM, "Item API call failed for $slug", t)
                        Toast.makeText(this@NewsActivity, "API Error", Toast.LENGTH_SHORT).show()
                    }
                })
        } ?: run {
            Log.e(TAG_ITEM, "Token is null, cannot fetch item $slug")
        }
    }
}
