package com.example.d3grimoire.posts.news

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import com.example.d3grimoire.R
import com.example.d3grimoire.posts.NewsData

class NewsScreen : Fragment(R.layout.activity_news) {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState);

        newsButtonData = listOf(
            NewsData(
                R.id.news_post_1,
                getString(R.string.news_1_title),
                getString(R.string.news_1_description),
                getString(R.string.news_1_imgUrl),
                getString(R.string.news_1_url),
                getString(R.string.news_1_author),
            ),
            NewsData(
                R.id.news_post_2,
                getString(R.string.news_2_title),
                getString(R.string.news_2_description),
                getString(R.string.news_2_imgUrl),
                getString(R.string.news_2_url),
                getString(R.string.news_2_author),
            ),
            NewsData(
                R.id.news_post_3,
                getString(R.string.news_3_title),
                getString(R.string.news_3_description),
                getString(R.string.news_3_imgUrl),
                getString(R.string.news_3_url),
                getString(R.string.news_3_author),
            )
        );

        newsButtonData.forEach { data ->
            val newsPostButton: NewsPostButtonActivity = NewsPostButtonActivity.newInstance(data);
            childFragmentManager.commit {
                setReorderingAllowed(true);
                add(data.id, newsPostButton);
            }
        }
    }
}