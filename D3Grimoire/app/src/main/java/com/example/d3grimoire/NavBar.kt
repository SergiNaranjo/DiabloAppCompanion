package com.example.d3grimoire

import android.os.Bundle
import android.view.View
import android.widget.ImageButton
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import com.example.d3grimoire.posts.community.CommunityScreen
import com.example.d3grimoire.posts.news.NewsScreen
import com.google.android.material.bottomnavigation.BottomNavigationView

class NavBar : AppCompatActivity() {

    enum class PostScreen {
        NEWS, COMMUNITY
    }

    private lateinit var bottomNavigationView: BottomNavigationView;
    private lateinit var floatingButtons: LinearLayout;
    private lateinit var postScreen: PostScreen;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navbar);
        bottomNavigationView = findViewById<BottomNavigationView>(R.id.navigation_bar);

        bottomNavigationView.setOnItemSelectedListener { item ->
            handleNavigationItemSelected(item.itemId);
        }

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

        loadFragment(NewsScreen());
    }

    private fun loadFragment(fragment: Fragment) {
        supportFragmentManager.commit {
            setReorderingAllowed(true);
            add(R.id.navbar_fragment, fragment);
        }
    }

    private fun handleNavigationItemSelected(itemId: Int): Boolean {
        return when (itemId) {
            R.id.news -> {
                when (postScreen) {
                    PostScreen.NEWS -> {
                        loadFragment(NewsScreen())
                    }
                    PostScreen.COMMUNITY -> {
                        loadFragment(CommunityScreen())
                    }
                }
                floatingButtons.visibility = View.VISIBLE;
                true
            }
            R.id.info -> {
                loadFragment(InfoScreen())
                floatingButtons.visibility = View.GONE;
                true
            }
            R.id.profile -> {
                loadFragment(ProfileActivity())
                floatingButtons.visibility = View.GONE;
                true
            }
            else -> false
        }
    }
}