package com.example.d3grimoire

import android.content.Intent
import android.os.Bundle
import android.os.Debug
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

        newsButtonData.forEach { data ->
            val intent : Intent = Intent(this, NewsPost::class.java);
            intent.putExtra("url", data.url);

            val newsPostButton: NewsPostButton = NewsPostButton.newInstance(
                data,
                { startActivity(intent); }
            );

            supportFragmentManager.commit {
                setReorderingAllowed(true);
                add(data.id, newsPostButton);
            }
        }
    }
}