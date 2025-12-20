package com.example.d3grimoire

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import com.example.d3grimoire.posts.news.NewsScreen
import com.google.android.material.bottomnavigation.BottomNavigationView

class NavBar : AppCompatActivity() {

    private lateinit var bottomNavigationView: BottomNavigationView;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navbar);
        bottomNavigationView = findViewById<BottomNavigationView>(R.id.navigation_bar);

        bottomNavigationView.setOnItemSelectedListener { item ->
            handleNavigationItemSelected(item.itemId);
        }

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
                loadFragment(NewsScreen())
                true
            }
            R.id.info -> {
                loadFragment(InfoScreen())
                true
            }
            R.id.profile -> {
                loadFragment(ProfileActivity())
                true
            }
            else -> false
        }
    }
}