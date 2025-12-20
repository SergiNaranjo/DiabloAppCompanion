package com.example.d3grimoire

import android.content.Intent
import android.os.Bundle
import android.os.Debug
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.add
import androidx.fragment.app.commit

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