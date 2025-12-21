package com.example.d3grimoire

import API.DiabloApiInstance
import API.model.ItemResponse
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.add
import androidx.fragment.app.commit
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class InfoScreen : Fragment(R.layout.info_screen) {

    companion object {
        private const val TAG_ITEM = "ITEM_API"
        private val testItemSlugs = listOf(
            "corrupted-ashbringer-Unique_Sword_2H_104_x1",
            "Helm-Barbarian",
            "HyperionSpear"
        )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        testItemSlugs.forEachIndexed { index, slug ->
            loadItem(slug, index + 1)
        }
    }

    private fun loadItem(itemSlug: String, containerIndex: Int) {
        DiabloApiInstance.api.getItem(itemSlug).enqueue(object : Callback<ItemResponse> {
            override fun onResponse(call: Call<ItemResponse>, response: Response<ItemResponse>) {
                if (!response.isSuccessful) {
                    Log.e(TAG_ITEM, "Item $itemSlug failed: ${response.code()}")
                    return
                }

                val item = response.body() ?: return
                Log.d(TAG_ITEM, "Loaded item: ${item.name}")

                val bundle = bundleOf(
                    "name" to item.name,
                    "type" to item.icon,
                    "requiredLevel" to item.requiredLevel,
                    "desc" to "Damage: ${item.damage}\nAPS: ${item.attacksPerSecond}"
                )

                val containerId = resources.getIdentifier(
                    "item_$containerIndex",
                    "id",
                    requireContext().packageName
                )

                if (containerId != 0) {
                    childFragmentManager.commit {
                        setReorderingAllowed(true)
                        add<Item>(containerId, args = bundle)
                    }
                }
            }

            override fun onFailure(call: Call<ItemResponse>, t: Throwable) {
                Log.e(TAG_ITEM, "Item API error for $itemSlug", t)
            }
        })
    }
}
