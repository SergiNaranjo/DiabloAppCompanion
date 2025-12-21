package com.example.d3grimoire.posts.news

import API.DiabloApiInstance
import API.model.HeroClassResponse
import API.model.ItemResponse
import API.model.SkillResponse
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.example.d3grimoire.R
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class NewsActivity : AppCompatActivity() {

    companion object {
        private const val TAG_CLASS = "CLASS_API"
        private const val TAG_SKILL = "SKILL_API"
        private const val TAG_ITEM = "ITEM_API"
    }

    private val testItemSlug =
        "corrupted-ashbringer-Unique_Sword_2H_104_x1"

    private val heroSlugs = listOf(
        "barbarian",
        "crusader",
        "demon-hunter",
        "monk",
        "necromancer",
        "witch-doctor",
        "wizard"
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.news_screen)

        loadAllHeroClasses()
        loadSingleSkill()
        loadItem()
    }

    private fun loadAllHeroClasses() {
        heroSlugs.forEach { slug ->
            loadHero(slug)
        }
    }

    private fun loadHero(slug: String) {
        DiabloApiInstance.api.getHeroClass(slug)
            .enqueue(object : Callback<HeroClassResponse> {

                override fun onResponse(
                    call: Call<HeroClassResponse>,
                    response: Response<HeroClassResponse>
                ) {
                    if (!response.isSuccessful) {
                        Log.e(TAG_CLASS, "Failed $slug: ${response.code()}")
                        return
                    }

                    val hero = response.body() ?: return

                    Log.d(TAG_CLASS, "Hero: ${hero.name}")
                    Log.d(TAG_CLASS, "Male: ${hero.maleName}, Female: ${hero.femaleName}")

                    hero.skills.active.forEach {
                        Log.d(TAG_CLASS, "Active: ${it.name}")
                    }

                    hero.skills.passive.forEach {
                        Log.d(TAG_CLASS, "Passive: ${it.name}")
                    }
                }

                override fun onFailure(call: Call<HeroClassResponse>, t: Throwable) {
                    Log.e(TAG_CLASS, "API error for $slug", t)
                }
            })
    }

    private fun loadSingleSkill() {
        DiabloApiInstance.api.getSkill(
            classSlug = "barbarian",
            skillSlug = "bash"
        ).enqueue(object : Callback<SkillResponse> {

            override fun onResponse(
                call: Call<SkillResponse>,
                response: Response<SkillResponse>
            ) {
                if (response.isSuccessful) {
                    Log.d(TAG_SKILL, "Skill: ${response.body()?.name}")
                } else {
                    Log.e(TAG_SKILL, "Skill failed: ${response.code()}")
                }
            }

            override fun onFailure(call: Call<SkillResponse>, t: Throwable) {
                Log.e(TAG_SKILL, "Skill API error", t)
            }
        })
    }

    private fun loadItem() {
        DiabloApiInstance.api.getItem(testItemSlug)
            .enqueue(object : Callback<ItemResponse> {

                override fun onResponse(
                    call: Call<ItemResponse>,
                    response: Response<ItemResponse>
                ) {
                    if (!response.isSuccessful) {
                        Log.e(TAG_ITEM, "Item failed: ${response.code()}")
                        return
                    }

                    val item = response.body() ?: return

                    Log.d(TAG_ITEM, "Item name: ${item.name}")
                    Log.d(TAG_ITEM, "Level: ${item.itemLevel}")
                    Log.d(TAG_ITEM, "Required level: ${item.requiredLevel}")
                    Log.d(TAG_ITEM, "Damage: ${item.damage}")
                    Log.d(TAG_ITEM, "APS: ${item.attacksPerSecond}")
                }

                override fun onFailure(call: Call<ItemResponse>, t: Throwable) {
                    Log.e(TAG_ITEM, "Item API error", t)
                }
            })
    }
}
