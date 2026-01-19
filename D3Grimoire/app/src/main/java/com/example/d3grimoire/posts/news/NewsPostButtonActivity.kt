package com.example.d3grimoire.posts.news

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import com.example.d3grimoire.FirebaseHandler
import com.example.d3grimoire.R
import com.example.d3grimoire.Utils
import com.example.d3grimoire.posts.NewsData
import java.net.URL
import java.util.concurrent.Executors

class NewsPostButtonActivity : Fragment(R.layout.activity_news_post_button) {
    companion object {
        const val KEY_TITLE: String = "title"
        const val KEY_DESC: String = "desc"
        const val KEY_IMG_URL: String = "imgUrl"
        const val KEY_URL: String = "url"
        const val KEY_AUTHOR: String = "author"

        fun newInstance(
            data: NewsData
        ): NewsPostButtonActivity {
            return NewsPostButtonActivity().apply {
                arguments = bundleOf(
                    KEY_TITLE to data.name,
                    KEY_DESC to data.description,
                    KEY_IMG_URL to data.imgUrl,
                    KEY_URL to data.url,
                    KEY_AUTHOR to data.author
                )
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val title : String? = requireArguments().getString(KEY_TITLE);
        var textView : TextView = view.findViewById<TextView>(R.id.news_post_title);
        textView.text = title;

        val description : String? = requireArguments().getString(KEY_DESC);
        textView = view.findViewById<TextView>(R.id.news_post_description);
        textView.text = description;

        Utils.trySetImageFromURL(
            requireArguments().getString(KEY_IMG_URL),
            view.findViewById<ImageView>(R.id.news_post_image)
        );

        view.findViewById<View>(R.id.news_post_button).setOnClickListener { onClick(); }
    }

    fun onClick() {
        FirebaseHandler.analyticsLogPostSelected(requireActivity(), this);
        val intent: Intent = Intent(requireActivity(), NewsPostActivity::class.java);
        intent.putExtra(NewsPostActivity.EXTRA_URL, requireArguments().getString(KEY_URL));
        startActivity(intent);
    }
}
