package com.example.d3grimoire

import android.os.Bundle
import android.widget.ImageButton
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import android.view.View
import com.example.d3grimoire.information.InformationActivity
import com.example.d3grimoire.posts.community.CommunityActivity
import com.example.d3grimoire.posts.news.NewsScreen
import com.example.d3grimoire.profile.ProfileActivity
import com.google.firebase.database.FirebaseDatabase


class NavBarActivity : AppCompatActivity() {
    enum class PostScreen {
        NEWS, COMMUNITY
    }
    private lateinit var btnNews: ImageButton
    private lateinit var btnInfo: ImageButton
    private lateinit var btnProfile: ImageButton
    private lateinit var postScreen: PostScreen
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

        val communityScreenButton: ImageButton = findViewById<ImageButton>(R.id.btnOther);
        val newsScreenButton: ImageButton = findViewById<ImageButton>(R.id.btnSettings);
        communityScreenButton.setOnClickListener {
            postScreen = PostScreen.COMMUNITY;
            loadFragment(CommunityActivity());
            communityScreenButton.setBackgroundResource(R.drawable.ic_btn_community_active);
            newsScreenButton.setBackgroundResource(R.drawable.ic_btn_news_inactive);
        }
        newsScreenButton.setOnClickListener {
            postScreen = PostScreen.NEWS;
            loadFragment(NewsScreen());
            communityScreenButton.setBackgroundResource(R.drawable.ic_btn_community_inactive);
            newsScreenButton.setBackgroundResource(R.drawable.ic_btn_news_active);
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
                    loadFragment(CommunityActivity())
                }
            }
            setFloatingButtonsVisibility(View.VISIBLE);
        }

        btnInfo.setOnClickListener {
            selectTab(btnInfo)
            loadFragment(InformationActivity())
            setFloatingButtonsVisibility(View.GONE);
        }

        btnProfile.setOnClickListener {
            selectTab(btnProfile)
            loadFragment(ProfileActivity())
            setFloatingButtonsVisibility(View.GONE);
        }
    }

    private fun selectTab(selected: ImageButton) {
        btnNews.isSelected = false
        btnInfo.isSelected = false
        btnProfile.isSelected = false

        selected.isSelected = true
    }

    public fun loadFragment(fragment: Fragment) {
        supportFragmentManager.commit {
            setReorderingAllowed(true)
            replace(R.id.fragment_container, fragment)
        }
    }

    public fun setFloatingButtonsVisibility(state: Int) {
        floatingButtons.visibility = state;
    }
}
