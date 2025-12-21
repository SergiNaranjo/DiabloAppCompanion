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
import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import com.example.d3grimoire.R
import com.example.d3grimoire.UserHandler
import com.example.d3grimoire.posts.NewsData
import com.google.firebase.analytics.FirebaseAnalytics
import java.net.URL
import java.util.concurrent.Executors

class NewsPostButton : Fragment(R.layout.news_post_button) {
    companion object {
        fun newInstance(
            data: NewsData
        ): NewsPostButton {
            return NewsPostButton().apply {
                arguments = bundleOf(
                    "title" to data.name,
                    "desc" to data.description,
                    "imgUrl" to data.imgUrl,
                    "url" to data.url,
                    "author" to data.author
                )
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val title : String? = requireArguments().getString("title");
        var textView : TextView = view.findViewById<TextView>(R.id.news_post_title);
        textView.text = title;

        val description : String? = requireArguments().getString("desc");
        textView = view.findViewById<TextView>(R.id.news_post_description);
        textView.text = description;

        val imageView = view.findViewById<ImageView>(R.id.news_post_image)
        val executor = Executors.newSingleThreadExecutor()
        val handler = Handler(Looper.getMainLooper())
        var image: Bitmap?;

        executor.execute {
            val imageURL = requireArguments().getString("imgUrl");
            try {
                val `in` = URL(imageURL).openStream()
                image = BitmapFactory.decodeStream(`in`)
                handler.post {
                    imageView.setImageBitmap(image)
                }
            }
            catch (e: Exception) {
                e.printStackTrace()
            }
        }

        view.findViewById<View>(R.id.news_post_button).setOnClickListener { onClick(); }
    }

    fun onClick() {
        requireActivity().run {
            val act = requireActivity();
            if (act is AppCompatActivity) {
                var user: String?;
                if (UserHandler.getUserGoogle(act) != null) {
                    user = UserHandler.getUserGoogle(act)!!.displayName;
                } else if (UserHandler.getUserNative(act) != null) {
                    user = UserHandler.getUserNative(act);
                } else {
                    user = "";
                }
                val bundle: Bundle = bundleOf(
                    "post_click_user" to user,
                    "post_click_author" to requireArguments().getString("author")
                );
                FirebaseAnalytics.getInstance(requireActivity())
                    .logEvent("PostSelected", bundle);
            }
            val intent: Intent = Intent(requireActivity(), NewsPost::class.java);
            intent.putExtra("url", requireArguments().getString("url"));
            startActivity(intent);
        }
    }
}