package com.example.d3grimoire.information

import API.DiabloImageUrl
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import coil.load
import com.example.d3grimoire.NavBarActivity
import com.example.d3grimoire.R
import com.example.d3grimoire.Utils

class ClassAbilityActivity : Fragment(R.layout.activity_class_ability) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState);

        val name: String? =
            arguments?.getString(getString(R.string.class_ability_name_key));
        val level: Int =
            arguments?.getInt(getString(R.string.class_ability_level_key)) ?: 0;
        val cost: Int =
            arguments?.getInt(getString(R.string.class_ability_cost_key)) ?: 0;
        val costUnits: String =
            arguments?.getString(getString(R.string.class_ability_costunits_key))
                ?: getString(R.string.class_ability_cost_default);
        val description: String? =
            arguments?.getString(getString(R.string.class_ability_description_key));
        val iconName: String =
            arguments?.getString(getString(R.string.class_ability_icon_key))
                ?: getString(R.string.class_ability_icon_default);

        val nameText: TextView = view.findViewById(R.id.class_ability_name);
        val levelText: TextView = view.findViewById(R.id.class_ability_level);
        val costText: TextView = view.findViewById(R.id.class_ability_cost);
        val descriptionText: TextView = view.findViewById(R.id.class_ability_desc);

        nameText.text = name;
        levelText.text = getString(R.string.unlocked_at_level, level);
        costText.text = getString(R.string.cost, cost, costUnits);
        descriptionText.text = description

        Utils.trySetImageFromURL(
            DiabloImageUrl.skill(requireActivity(), iconName),
            view.findViewById(R.id.class_ability_icon)
        )
    }
}