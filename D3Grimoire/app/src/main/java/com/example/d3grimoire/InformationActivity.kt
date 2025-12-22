package com.example.d3grimoire

import API.DiabloApiInstance
import API.DiabloImageUrl
import API.model.ItemResponse
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.FrameLayout
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class InformationActivity : Fragment(R.layout.activity_information) {

    companion object {
        private const val TAG_ITEM = "ITEM_API"
        private val testItemSlugs = listOf(
            "corrupted-ashbringer-Unique_Sword_2H_104_x1",
            "aidans-revenge-Unique_Axe_1H_104_x1"
        )
    }

    private val containers = listOf(R.id.item_0, R.id.item_1)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        testItemSlugs.forEachIndexed { index, slug ->
            if (index < containers.size) {
                loadItem(slug, containers[index])
            }
        }

        setupClassButton(view, R.id.btn_barbarian, "barbarian")
        setupClassButton(view, R.id.btn_crusader, "crusader")
        setupClassButton(view, R.id.btn_demon_hunter, "demon-hunter")
        setupClassButton(view, R.id.btn_monk, "monk")
        setupClassButton(view, R.id.btn_necromancer, "necromancer")
        setupClassButton(view, R.id.btn_witch_doctor, "witch-doctor")
        setupClassButton(view, R.id.btn_wizard, "wizard")
    }

    private fun setupClassButton(view: View, buttonId: Int, slug: String) {
        view.findViewById<FrameLayout>(buttonId)?.setOnClickListener {
            val intent = Intent(requireContext(), ClassInformationActivity::class.java)
            intent.putExtra("HERO_SLUG", slug)
            startActivity(intent)
        }
    }

    private fun loadItem(itemSlug: String, containerId: Int) {
        DiabloApiInstance.api.getItem(itemSlug).enqueue(object : Callback<ItemResponse> {
            override fun onResponse(call: Call<ItemResponse>, response: Response<ItemResponse>) {
                if (!isAdded || view == null) return

                if (!response.isSuccessful) {
                    Log.e(TAG_ITEM, "Item $itemSlug failed: ${response.code()}")
                    return
                }

                val item = response.body() ?: return

                val bundle = bundleOf(
                    "name" to item.name,
                    "type" to item.typeName,
                    "iconUrl" to DiabloImageUrl.item(item.icon),
                    "requiredLevel" to item.requiredLevel,
                    "desc" to "Damage: ${item.damage}\nAPS: ${item.attacksPerSecond}"
                )

                childFragmentManager.commit {
                    setReorderingAllowed(true)
                    replace(containerId, ItemActivity::class.java, bundle)
                }
            }

            override fun onFailure(call: Call<ItemResponse>, t: Throwable) {
                Log.e(TAG_ITEM, "Item API error for $itemSlug", t)
            }
        })
    }
}