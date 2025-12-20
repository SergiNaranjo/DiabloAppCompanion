package com.example.d3grimoire.posts.community

import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.d3grimoire.R

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


    }
}