package com.example.d3grimoire

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.add
import androidx.fragment.app.commit

class NewsScreen : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.news_screen);

        val intent : Intent = Intent(this, NewsPost::class.java);
        intent.putExtra("url", "https://news.blizzard.com/en-us/article/24191146/season-35-eternal-conflict-has-concluded");

        var newsPostButton : NewsPostButton = NewsPostButton.newInstance(
            "Juega a Diablo III en Game Pass!",
            "Un nuevo pack de recompensas legendarias...",
            { startActivity(intent) }
        )
        supportFragmentManager.commit {
            setReorderingAllowed(true);
            add(R.id.news_post_1, newsPostButton);
        }

        newsPostButton = NewsPostButton.newInstance(
            "Juega a Diablo III en Game Pass!",
            "Un nuevo pack de recompensas legendarias...",
            { startActivity(intent) }
        );
        supportFragmentManager.commit {
            setReorderingAllowed(true);
            add(R.id.news_post_2, newsPostButton);
        }

        newsPostButton = NewsPostButton.newInstance(
            "Juega a Diablo III en Game Pass!",
            "Un nuevo pack de recompensas legendarias...",
            { startActivity(intent) }
        );
        supportFragmentManager.commit {
            setReorderingAllowed(true);
            add(R.id.news_post_3, newsPostButton);
        }
    }
}