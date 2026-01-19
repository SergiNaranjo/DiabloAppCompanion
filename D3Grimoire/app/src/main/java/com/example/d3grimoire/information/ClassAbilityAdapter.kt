package com.example.d3grimoire.information

import API.DiabloImageUrl
import API.model.HeroSkill
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.d3grimoire.R

class ClassAbilityAdapter : RecyclerView.Adapter<ClassAbilityAdapter.SkillViewHolder>() {

    private var items: List<HeroSkill> = emptyList()
    private companion object {
        private const val DEFAULT_SKILL_COST: Int = 0
    }

    fun submitList(skills: List<HeroSkill>) {
        items = skills
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SkillViewHolder {
        val view: View = LayoutInflater.from(parent.context)
            .inflate(R.layout.activity_class_ability, parent, false)
        return SkillViewHolder(view)
    }

    override fun onBindViewHolder(holder: SkillViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount(): Int = items.size

    class SkillViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val nameText: TextView = itemView.findViewById(R.id.class_ability_name)
        private val levelText: TextView = itemView.findViewById(R.id.class_ability_level)
        private val costText: TextView = itemView.findViewById(R.id.class_ability_cost)
        private val descriptionText: TextView = itemView.findViewById(R.id.class_ability_desc)
        private val iconView: ImageView = itemView.findViewById(R.id.class_ability_icon)

        fun bind(skill: HeroSkill) {
            nameText.text = skill.name
            levelText.text = itemView.context.getString(R.string.unlocked_at_level, skill.level)
            costText.text = itemView.context.getString(
                R.string.cost,
                DEFAULT_SKILL_COST,
                itemView.context.getString(R.string.class_ability_cost_default)
            )
            descriptionText.text = skill.description
            iconView.load(DiabloImageUrl.skill(itemView.context, skill.icon)) {
                crossfade(true)
            }
        }
    }
}
