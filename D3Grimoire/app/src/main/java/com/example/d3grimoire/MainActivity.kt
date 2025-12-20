package com.example.d3grimoire

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.bundleOf
import androidx.fragment.app.add
import androidx.fragment.app.commit
import com.google.firebase.Firebase
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.analytics

class MainActivity : AppCompatActivity() {

    private lateinit var analytics: FirebaseAnalytics;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.splash_screen)

        analytics = Firebase.analytics;
        analytics.logEvent("Help", null);

        startActivity(Intent(this, NavBar::class.java));
    }
}