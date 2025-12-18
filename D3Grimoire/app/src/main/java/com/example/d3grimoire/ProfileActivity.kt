package com.example.d3grimoire

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.bundleOf
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.add
import androidx.fragment.app.commit

class ProfileActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.profile_screen)

        var bundle : Bundle = bundleOf(
            "name" to "Name",
            "class" to "Barbarian",
            "level" to 5
        );
        supportFragmentManager.commit {
            setReorderingAllowed(true);
            add<ProfileHero>(R.id.hero_1, args = bundle);
        }

        bundle = bundleOf(
            "name" to "Name",
            "class" to "Barbarian",
            "level" to 5
        );
        supportFragmentManager.commit {
            setReorderingAllowed(true);
            add<ProfileHero>(R.id.hero_2, args = bundle);
        }

        bundle = bundleOf(
            "name" to "Name",
            "class" to "Barbarian",
            "level" to 5
        );
        supportFragmentManager.commit {
            setReorderingAllowed(true);
            add<ProfileHero>(R.id.hero_3, args = bundle);
        }
    }
}