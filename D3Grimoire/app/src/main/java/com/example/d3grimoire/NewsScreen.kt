package com.example.d3grimoire

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.bundleOf
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.add
import androidx.fragment.app.commit

class NewsScreen : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.news_screen);

        var bundle : Bundle = bundleOf(
            "title" to "Juega a Diablo III en Game Pass!",
            "desc" to "Un nuevo pack de recompensas legendarias..."
        );
        supportFragmentManager.commit {
            setReorderingAllowed(true);
            add<NewsPost>(R.id.news_post_1, args = bundle);
        }

        bundle = bundleOf(
            "title" to "Juega a Diablo III en Game Pass!",
            "desc" to "Un nuevo pack de recompensas legendarias..."
        );
        supportFragmentManager.commit {
            setReorderingAllowed(true);
            add<NewsPost>(R.id.news_post_2, args = bundle);
        }

        bundle = bundleOf(
            "title" to "Juega a Diablo III en Game Pass!",
            "desc" to "Un nuevo pack de recompensas legendarias..."
        );
        supportFragmentManager.commit {
            setReorderingAllowed(true);
            add<NewsPost>(R.id.news_post_3, args = bundle);
        }
    }
}