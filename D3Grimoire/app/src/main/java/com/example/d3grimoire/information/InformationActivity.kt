package com.example.d3grimoire.information

import API.DiabloApiInstance
import API.DiabloImageUrl
import API.model.ItemResponse
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.FrameLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.commit
import com.example.d3grimoire.NavBarActivity
import com.example.d3grimoire.R
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
                loadItem(slug, containers[index]);
            }
        }

        setupClassButton(view,
            R.id.information_barbarian_button,
            getString(R.string.barbarian_slug));
        setupClassButton(view,
            R.id.information_crusader_button,
            getString(R.string.crusader_slug));
        setupClassButton(view,
            R.id.information_demon_hunter_button,
            getString(R.string.demon_hunter_slug));
        setupClassButton(view,
            R.id.information_monk_button,
            getString(R.string.monk_slug));
        setupClassButton(view,
            R.id.information_necromancer_button,
            getString(R.string.necromancer_slug));
        setupClassButton(view,
            R.id.information_witch_doctor_button,
            getString(R.string.witch_doctor_slug));
        setupClassButton(view,
            R.id.information_wizard_button,
            getString(R.string.wizard_slug));
    }

    private fun setupClassButton(view: View, buttonId: Int, slug: String) {
        view.findViewById<FrameLayout>(buttonId)?.setOnClickListener {
            val intent: Intent = Intent(requireContext(), ClassInformationActivity::class.java);
            intent.putExtra(getString(R.string.class_information_hero_slug_key), slug);
            startActivity(intent);
        }
    }

    private fun loadItem(itemSlug: String, containerId: Int) {
        DiabloApiInstance.api.getItem(itemSlug).enqueue(object : Callback<ItemResponse> {
            override fun onResponse(call: Call<ItemResponse>, response: Response<ItemResponse>) {
                if (!isAdded || view == null) return;

                if (!response.isSuccessful) {
                    Log.e(TAG_ITEM, "Item $itemSlug failed: ${response.code()}");
                    return;
                }

                val item: ItemResponse = response.body() ?: return;

                val bundle = bundleOf(
                    getString(R.string.item_name_key) to item.name,
                    getString(R.string.item_type_key) to item.typeName,
                    getString(R.string.item_required_level_key) to item.requiredLevel,
                    getString(R.string.item_description_key) to getString(
                        R.string.item_description,
                        item.damage,
                        item.attacksPerSecond
                    ),
                    getString(R.string.item_icon_url_key) to DiabloImageUrl.item(requireActivity(), item.icon)
                );

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