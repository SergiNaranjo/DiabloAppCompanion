package com.example.d3grimoire

import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.fragment.app.Fragment

class ClassAbility : Fragment(R.layout.class_ability) {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val name : String? = requireArguments().getString("name");
        var textView : TextView = view.findViewById<TextView>(R.id.class_ability_name);
        textView.text = name;

        val cost : Int? = requireArguments().getInt("cost");
        val costUnits : String? = requireArguments().getString("costUnits");
        textView = view.findViewById<TextView>(R.id.class_ability_cost);
        textView.text = "Cost: " + cost.toString() + " " + costUnits;

        val description : String? = requireArguments().getString("desc");
        textView = view.findViewById<TextView>(R.id.class_ability_desc);
        textView.text = description;
    }
}