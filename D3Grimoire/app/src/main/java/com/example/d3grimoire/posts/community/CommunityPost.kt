package com.example.d3grimoire.posts.community

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.d3grimoire.R
import java.net.URL
import java.util.concurrent.Executors

class CommunityPost : Fragment(R.layout.activity_community_post) {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        //Load card title
        val cardTitle : String? = requireArguments().getString("title");
        var textView : TextView = view.findViewById<TextView>(R.id.cardTitle);
        textView.text = cardTitle;

        //Load description
        val cardDescription : String? = requireArguments().getString("desc");
        textView = view.findViewById<TextView>(R.id.cardDescription);
        textView.text = cardDescription;

        //Load description
        val cardAuthor : String? = requireArguments().getString("author");
        textView = view.findViewById<TextView>(R.id.cardPosted);
        textView.text = cardAuthor;

        //Load image
        val imageView = view.findViewById<ImageView>(R.id.cardImage)
        val executor = Executors.newSingleThreadExecutor()
        val handler = Handler(Looper.getMainLooper())
        var image: Bitmap? = null
        executor.execute {
            try {
                val `in` = URL(requireArguments().getString("imgUrl")).openStream()
                image = BitmapFactory.decodeStream(`in`)

                handler.post {
                    imageView.setImageBitmap(image)
                }
            }
            catch (e: Exception) {
                e.printStackTrace()
            }
        }

        //view.findViewById<View>(R.id.news_post_button).setOnClickListener { this.onClickListener?.invoke(); }


    }
}