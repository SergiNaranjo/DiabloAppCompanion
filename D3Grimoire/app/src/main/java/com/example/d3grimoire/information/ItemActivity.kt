package com.example.d3grimoire.information

import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import coil.load
import com.example.d3grimoire.R

class ItemActivity : Fragment(R.layout.activity_item) {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val name = arguments?.getString("name")
        val type = arguments?.getString("type")
        val requiredLevel = arguments?.getInt("requiredLevel") ?: 0
        val description = arguments?.getString("desc")
        val iconUrl = arguments?.getString("iconUrl")

        val nameTv: TextView = view.findViewById(R.id.item_name)
        val typeTv: TextView = view.findViewById(R.id.item_type)
        val levelTv: TextView = view.findViewById(R.id.item_level_required)
        val descTv: TextView = view.findViewById(R.id.item_description)
        val iconIv: ImageView = view.findViewById(R.id.item_icon)

        nameTv.text = name
        typeTv.text = type
        levelTv.text = "REQUIRES LEVEL: $requiredLevel"
        descTv.text = description

        iconIv.load(iconUrl) {
            crossfade(true)
            placeholder(android.R.drawable.progress_horizontal)
            error(android.R.drawable.stat_notify_error)
        }
    }
}