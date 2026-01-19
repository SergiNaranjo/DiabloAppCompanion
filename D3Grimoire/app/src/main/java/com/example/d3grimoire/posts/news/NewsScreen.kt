package com.example.d3grimoire.posts.news

import android.os.Bundle
import android.view.View
import android.content.Intent
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.d3grimoire.FirebaseHandler
import com.example.d3grimoire.R
import com.example.d3grimoire.posts.NewsData
import com.example.d3grimoire.posts.NewsPostAdapter

class NewsScreen : Fragment(R.layout.activity_news) {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState);

        val recyclerView: RecyclerView = view.findViewById<RecyclerView>(R.id.news_posts_recycler)
        val adapter = NewsPostAdapter { data -> openPost(data) }
        recyclerView.layoutManager = LinearLayoutManager(view.context)
        recyclerView.adapter = adapter

        newsButtonData = listOf(
            NewsData(
                0,
                getString(R.string.news_1_title),
                getString(R.string.news_1_description),
                getString(R.string.news_1_imgUrl),
                getString(R.string.news_1_url),
                getString(R.string.news_1_author),
            ),
            NewsData(
                1,
                getString(R.string.news_2_title),
                getString(R.string.news_2_description),
                getString(R.string.news_2_imgUrl),
                getString(R.string.news_2_url),
                getString(R.string.news_2_author),
            ),
            NewsData(
                2,
                getString(R.string.news_3_title),
                getString(R.string.news_3_description),
                getString(R.string.news_3_imgUrl),
                getString(R.string.news_3_url),
                getString(R.string.news_3_author),
            )
        );

        adapter.submitList(newsButtonData)
    }

    private fun openPost(data: NewsData) {
        FirebaseHandler.analyticsLogPostSelected(requireActivity(), data);
        val intent: Intent = Intent(requireActivity(), NewsPostActivity::class.java);
        intent.putExtra("url", data.url);
        startActivity(intent);
    }
}
