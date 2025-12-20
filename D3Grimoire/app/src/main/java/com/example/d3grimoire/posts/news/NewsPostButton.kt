package com.example.d3grimoire.posts.news

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
import com.example.d3grimoire.R
import java.net.URL
import java.util.concurrent.Executors

class NewsPostButton : Fragment(R.layout.news_post_button) {

    private var onClickListener: (() -> Unit)? = null;

    companion object {
        fun newInstance(
            data: NewsData,
            onClick: () -> Unit
        ): NewsPostButton {
            return NewsPostButton().apply {
                arguments = bundleOf(
                    "title" to data.name,
                    "desc" to data.description,
                    "imgId" to data.imgId
                )
                this.onClickListener = onClick
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

        //val imgId: Int? = requireArguments().getInt("imgId");
        //var imageView : ImageView = view.findViewById<ImageView>(R.id.news_post_image);
        //imageView.setImageResource(imgId!!);

        // Declaring and initializing the ImageView
        val imageView = view.findViewById<ImageView>(R.id.news_post_image)

        // Declaring executor to parse the URL
        val executor = Executors.newSingleThreadExecutor()

        // Once the executor parses the URL
        // and receives the image, handler will load it
        // in the ImageView
        val handler = Handler(Looper.getMainLooper())

        // Initializing the image
        var image: Bitmap? = null

        // Only for Background process (can take time depending on the Internet speed)
        executor.execute {

            // Image URL
            val imageURL = "https://media.geeksforgeeks.org/wp-content/cdn-uploads/gfg_200x200-min.png"
            println("executing");
            // Tries to get the image and post it in the ImageView
            // with the help of Handler
            try {
                val `in` = URL(imageURL).openStream()
                image = BitmapFactory.decodeStream(`in`)
                println("stream open");
                assert(imageView != null);
                assert(image != null);

                // Only for making changes in UI
                handler.post {
                    imageView.setImageBitmap(image)
                }
                println("posted");
            }

            // If the URL doesnot point to
            // image or any other kind of failure
            catch (e: Exception) {
                println("AAA")
                e.printStackTrace()
            }
        }

        view.findViewById<View>(R.id.news_post_button).setOnClickListener { this.onClickListener?.invoke(); }
    }
}