package com.example.d3grimoire.posts.news

import androidx.core.content.ContextCompat.getString
import com.example.d3grimoire.R
import com.example.d3grimoire.posts.NewsData

//Ideally, a separate API could be used for news.
// Unfortunately, Blizzard doesn't share their news through an API.
public lateinit var newsButtonData: List<NewsData>;