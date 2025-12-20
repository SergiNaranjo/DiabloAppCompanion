package com.example.d3grimoire

import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.fragment.app.Fragment

class ProfileHero : Fragment(R.layout.profile_hero) {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState);
        //Load card title
        val name : String? = requireArguments().getString("name");
        var textView : TextView = view.findViewById<TextView>(R.id.hero_name);
        textView.text = name;

        val classType : String? = requireArguments().getString("class");
        val level : String? = requireArguments().getInt("level").toString();
        textView = view.findViewById<TextView>(R.id.hero_class_level);
        textView.text = classType + " LVL. " + level;
    }
}