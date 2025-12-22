package com.example.d3grimoire

import API.DiabloImageUrl
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import coil.load

class ClassAbilityActivity : Fragment(R.layout.activity_class_ability) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val name = arguments?.getString("name")
        val level = arguments?.getInt("level") ?: 0
        val cost = arguments?.getInt("cost") ?: 0
        val costUnits = arguments?.getString("costUnits") ?: "Resource"
        val description = arguments?.getString("desc")
        val iconName = arguments?.getString("icon") ?: ""

        val nameTv: TextView = view.findViewById(R.id.class_ability_name)
        val levelTv: TextView = view.findViewById(R.id.class_ability_level)
        val costTv: TextView = view.findViewById(R.id.class_ability_cost)
        val descTv: TextView = view.findViewById(R.id.class_ability_desc)
        val iconIv: ImageView = view.findViewById(R.id.class_ability_icon)

        nameTv.text = name
        levelTv.text = "Unlocked at Level: $level"
        costTv.text = "Cost: $cost $costUnits"
        descTv.text = description

        iconIv.load(DiabloImageUrl.skill(iconName)) {
            crossfade(true)
            placeholder(android.R.drawable.ic_menu_report_image)
            error(android.R.drawable.ic_delete)
        }
    }
}