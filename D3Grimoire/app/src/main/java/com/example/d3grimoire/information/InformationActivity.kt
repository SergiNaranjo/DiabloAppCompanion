package com.example.d3grimoire.information

import API.DiabloApiInstance
import API.DiabloImageUrl
import API.model.ItemResponse
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.FrameLayout
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.d3grimoire.R
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class InformationActivity : Fragment(R.layout.activity_information) {

    companion object {
        private const val TAG_ITEM: String = "ITEM_API"
        private val testItemSlugs: List<String> = listOf(
            "corrupted-ashbringer-Unique_Sword_2H_104_x1",
            "the-furnace-Unique_Mace_2H_103_x1",
            "mempo-of-twilight-Unique_Helm_008_x1",
            "skorn-Unique_Axe_2H_009_x1",
            "andariels-visage-Unique_Helm_003_x1",
            "bul-kathos-wedding-band-Unique_Ring_020_x1",
            "echoing-fury-Unique_Mace_1H_001_x1",
            "the-grandfather-Unique_Sword_2H_010_x1",
            "unity-Unique_Ring_010_x1",
            "tasker-and-theo-Unique_Gloves_006_x1"
        )
    }

    private lateinit var itemAdapter: ItemAdapter
    private val itemSlots: MutableList<ItemUiModel?> =
        MutableList(testItemSlugs.size) { null }
    private val classButtons: List<Pair<Int, Int>> = listOf(
        R.id.information_barbarian_button to R.string.barbarian_slug,
        R.id.information_crusader_button to R.string.crusader_slug,
        R.id.information_demon_hunter_button to R.string.demon_hunter_slug,
        R.id.information_monk_button to R.string.monk_slug,
        R.id.information_necromancer_button to R.string.necromancer_slug,
        R.id.information_witch_doctor_button to R.string.witch_doctor_slug,
        R.id.information_wizard_button to R.string.wizard_slug
    )

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val itemsRecycler: RecyclerView = view.findViewById(R.id.items_recycler)
        itemAdapter = ItemAdapter()
        itemsRecycler.layoutManager = LinearLayoutManager(view.context)
        itemsRecycler.adapter = itemAdapter

        testItemSlugs.forEachIndexed { index, slug ->
            loadItem(slug, index);
        }

        classButtons.forEach { (buttonId, slugRes) ->
            setupClassButton(view, buttonId, getString(slugRes))
        }
    }

    private fun setupClassButton(view: View, buttonId: Int, slug: String) {
        view.findViewById<FrameLayout>(buttonId)?.setOnClickListener {
            val intent: Intent = Intent(requireContext(), ClassInformationActivity::class.java);
            intent.putExtra(getString(R.string.class_information_hero_slug_key), slug);
            startActivity(intent);
        }
    }

    private fun loadItem(itemSlug: String, index: Int) {
        DiabloApiInstance.api.getItem(itemSlug).enqueue(object : Callback<ItemResponse> {
            override fun onResponse(call: Call<ItemResponse>, response: Response<ItemResponse>) {
                if (!isAdded || view == null || childFragmentManager.isStateSaved) return;

                if (!response.isSuccessful) {
                    Log.e(TAG_ITEM, "Item $itemSlug failed: ${response.code()}");
                    return;
                }

                val item: ItemResponse = response.body() ?: return;

                val itemUi: ItemUiModel = ItemUiModel(
                    item.name,
                    item.typeName,
                    item.requiredLevel,
                    getString(
                        R.string.item_description,
                        item.damage,
                        item.attacksPerSecond
                    ),
                    DiabloImageUrl.item(requireActivity(), item.icon)
                )
                itemSlots[index] = itemUi
                // Keep the list in API order while items load asynchronously.
                itemAdapter.submitList(itemSlots.filterNotNull())
            }

            override fun onFailure(call: Call<ItemResponse>, t: Throwable) {
                Log.e(TAG_ITEM, "Item API error for $itemSlug", t)
            }
        })
    }
}
