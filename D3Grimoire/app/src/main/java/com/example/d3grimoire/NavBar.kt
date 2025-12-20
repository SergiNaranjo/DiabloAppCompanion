package com.example.d3grimoire

import android.os.Bundle
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit

class NavBar : AppCompatActivity() {

    private lateinit var btnNews: ImageButton
    private lateinit var btnInfo: ImageButton
    private lateinit var btnProfile: ImageButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_navbar)

        btnNews = findViewById(R.id.btnNews)
        btnInfo = findViewById(R.id.btnInfo)
        btnProfile = findViewById(R.id.btnProfile)

        setupListeners()

        // Fragment inicial
        selectTab(btnNews)
        loadFragment(NewsScreen())
    }

    private fun setupListeners() {
        btnNews.setOnClickListener {
            selectTab(btnNews)
            loadFragment(NewsScreen())
        }

        btnInfo.setOnClickListener {
            selectTab(btnInfo)
            loadFragment(InfoScreen())
        }

        btnProfile.setOnClickListener {
            selectTab(btnProfile)
            loadFragment(ProfileActivity())
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