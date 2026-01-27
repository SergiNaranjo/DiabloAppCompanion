package com.example.d3grimoire.information

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.d3grimoire.R

data class ItemUiModel(
    val name: String,
    val typeName: String,
    val requiredLevel: Int,
    val description: String,
    val iconUrl: String
)

class ItemAdapter : RecyclerView.Adapter<ItemAdapter.ItemViewHolder>() {

    private val items: MutableList<ItemUiModel> = mutableListOf()

    fun submitList(newItems: List<ItemUiModel>) {
        items.clear()
        items.addAll(newItems)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val view: View = LayoutInflater.from(parent.context)
            .inflate(R.layout.activity_item, parent, false)
        return ItemViewHolder(view)
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount(): Int = items.size

    class ItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val iconView: ImageView = itemView.findViewById(R.id.item_icon)
        private val nameView: TextView = itemView.findViewById(R.id.item_name)
        private val typeView: TextView = itemView.findViewById(R.id.item_type)
        private val levelView: TextView = itemView.findViewById(R.id.item_level_required)
        private val descriptionView: TextView = itemView.findViewById(R.id.item_description)

        fun bind(item: ItemUiModel) {
            nameView.text = item.name
            typeView.text = item.typeName
            levelView.text = itemView.context.getString(
                R.string.item_requires_level,
                item.requiredLevel
            )
            descriptionView.text = item.description
            iconView.load(item.iconUrl) {
                crossfade(true)
            }
        }
    }
}
