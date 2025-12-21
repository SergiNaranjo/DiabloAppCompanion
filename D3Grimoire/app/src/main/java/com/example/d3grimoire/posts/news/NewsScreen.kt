package com.example.d3grimoire.posts.news

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import com.example.d3grimoire.R
import com.example.d3grimoire.UserHandler
import com.google.firebase.analytics.FirebaseAnalytics

class NewsScreen : Fragment(R.layout.news_screen) {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState);

        newsButtonData.forEach { data ->
            val newsPostButton: NewsPostButton = NewsPostButton.newInstance(data);
            childFragmentManager.commit {
                setReorderingAllowed(true);
                add(data.id, newsPostButton);
            }
        }
    }
}