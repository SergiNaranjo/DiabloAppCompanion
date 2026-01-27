package com.example.d3grimoire.information

import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.d3grimoire.R
import com.example.d3grimoire.ImageDecoder

class ItemActivity : Fragment(R.layout.activity_item) {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState);

        val name: String? = arguments?.getString(getString(R.string.item_name_key));
        val type: String? = arguments?.getString(getString(R.string.item_type_key));
        val requiredLevel: Int = arguments?.getInt(getString(R.string.item_required_level_key)) ?: 0;
        val description: String? = arguments?.getString(getString(R.string.item_description_key));

        val nameText: TextView = view.findViewById(R.id.item_name);
        val typeText: TextView = view.findViewById(R.id.item_type);
        val levelText: TextView = view.findViewById(R.id.item_level_required);
        val descriptionText: TextView = view.findViewById(R.id.item_description);

        nameText.text = name;
        typeText.text = type;
        levelText.text = getString(R.string.item_requires_level, requiredLevel);
        descriptionText.text = description;

        ImageDecoder.trySetImageFromURL(
            arguments?.getString(getString(R.string.item_icon_url_key)),
            view.findViewById(R.id.item_icon)
        )
    }
}