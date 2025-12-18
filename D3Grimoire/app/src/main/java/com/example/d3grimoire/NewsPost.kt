package com.example.d3grimoire

import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.fragment.app.Fragment

class NewsPost : Fragment(R.layout.news_post) {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val title : String? = requireArguments().getString("title");
        var textView : TextView = view.findViewById<TextView>(R.id.news_post_title);
        textView.text = title;

        val description : String? = requireArguments().getString("desc");
        textView = view.findViewById<TextView>(R.id.news_post_description);
        textView.text = description;
    }
}