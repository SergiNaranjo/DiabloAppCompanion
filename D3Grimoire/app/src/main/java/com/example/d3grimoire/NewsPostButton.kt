package com.example.d3grimoire

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer

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

        val imgId: Int? = requireArguments().getInt("imgId");
        var imageView : ImageView = view.findViewById<ImageView>(R.id.news_post_image);
        imageView.setImageResource(imgId!!);

        view.findViewById<View>(R.id.news_post_button).setOnClickListener { this.onClickListener?.invoke(); }
    }
}