package com.example.d3grimoire

import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.fragment.app.Fragment

class Item : Fragment(R.layout.item_fragment) {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val name : String? = requireArguments().getString("name");
        var textView : TextView = view.findViewById<TextView>(R.id.item_name);
        textView.text = name;

        val type : String? = requireArguments().getString("type");
        textView = view.findViewById<TextView>(R.id.item_type);
        textView.text = type;

        val requiredLevel : Int? = requireArguments().getInt("requiredLevel");
        textView = view.findViewById<TextView>(R.id.item_level_required);
        textView.text = "REQUIRES LEVEL: " + requiredLevel.toString();

        val description : String? = requireArguments().getString("desc");
        textView = view.findViewById<TextView>(R.id.item_description);
        textView.text = description;
    }
}