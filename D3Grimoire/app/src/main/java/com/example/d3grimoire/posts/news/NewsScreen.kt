package com.example.d3grimoire.posts.news

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import com.example.d3grimoire.R

class NewsScreen : Fragment(R.layout.news_screen) {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState);

        newsButtonData.forEach { data ->
            val intent : Intent = Intent(requireActivity(), NewsPost::class.java);
            intent.putExtra("url", data.url);

            val newsPostButton: NewsPostButton = NewsPostButton.newInstance(
                data,
                {
                    requireActivity().run {
                        startActivity(intent);
                    }
                }
            );

            childFragmentManager.commit {
                setReorderingAllowed(true);
                add(data.id, newsPostButton);
            }
        }
    }
}