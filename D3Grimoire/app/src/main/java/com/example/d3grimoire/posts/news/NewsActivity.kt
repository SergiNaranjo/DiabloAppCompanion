package com.example.d3grimoire.posts.news

import API.DiabloApiInstance
import API.model.HeroClassResponse
import API.model.ItemResponse
import API.model.SkillResponse
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.example.d3grimoire.R
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class NewsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.news_screen)
    }
}
