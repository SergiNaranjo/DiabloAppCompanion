package com.example.d3grimoire

import android.os.Bundle
import android.widget.ImageButton
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import android.view.View
import com.example.d3grimoire.posts.community.CommunityScreen
import com.example.d3grimoire.posts.news.NewsScreen
import com.google.android.material.bottomnavigation.BottomNavigationView



class NavBar : AppCompatActivity() {
    enum class PostScreen {
        NEWS, COMMUNITY
    }
    private lateinit var btnNews: ImageButton
    private lateinit var btnInfo: ImageButton
    private lateinit var btnProfile: ImageButton

    private lateinit var floatingButtons: LinearLayout;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_navbar)

        btnNews = findViewById(R.id.btnNews)
        btnInfo = findViewById(R.id.btnInfo)
        btnProfile = findViewById(R.id.btnProfile)

        setupListeners()

        selectTab(btnNews)

        floatingButtons = findViewById<LinearLayout>(R.id.floatingButtons);

        val communityScreenButton: ImageButton = findViewById<ImageButton>(R.id.community_screen);
        val newsScreenButton: ImageButton = findViewById<ImageButton>(R.id.news_screen);
        communityScreenButton.setOnClickListener {
            postScreen = PostScreen.COMMUNITY;
            loadFragment(CommunityScreen());
            communityScreenButton.setBackgroundResource(R.drawable.circle_bg);
            newsScreenButton.setBackgroundResource(R.drawable.circle_bg_default);
        }
        newsScreenButton.setOnClickListener {
            postScreen = PostScreen.NEWS;
            loadFragment(NewsScreen());
            communityScreenButton.setBackgroundResource(R.drawable.circle_bg_default);
            newsScreenButton.setBackgroundResource(R.drawable.circle_bg);
        }

        postScreen = PostScreen.NEWS;
        loadFragment(NewsScreen())
    }

    private fun setupListeners() {
        btnNews.setOnClickListener {
            selectTab(btnNews)
            when (postScreen) {
                PostScreen.NEWS -> {
                    loadFragment(NewsScreen())
                }

                PostScreen.COMMUNITY -> {
                    loadFragment(CommunityScreen())
                }
            }
            floatingButtons.visibility = View.VISIBLE;
        }

        btnInfo.setOnClickListener {
            selectTab(btnInfo)
            loadFragment(InfoScreen())
            floatingButtons.visibility = View.GONE;
        }

        btnProfile.setOnClickListener {
            selectTab(btnProfile)
            loadFragment(ProfileActivity())
            floatingButtons.visibility = View.GONE;
        }
    }

    private fun selectTab(selected: ImageButton) {
        btnNews.isSelected = false
        btnInfo.isSelected = false
        btnProfile.isSelected = false

        selected.isSelected = true
    }

    private fun loadFragment(fragment: Fragment) {
        supportFragmentManager.commit {
            setReorderingAllowed(true)
            replace(R.id.fragment_container, fragment)
        }
    }
}