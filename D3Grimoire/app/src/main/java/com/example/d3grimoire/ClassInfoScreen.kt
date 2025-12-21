package com.example.d3grimoire

import API.DiabloApiInstance
import API.model.HeroClassResponse
import API.model.HeroSkill
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.bundleOf
import androidx.fragment.app.add
import androidx.fragment.app.commit
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ClassInfoScreen : AppCompatActivity() {

    companion object {
        private const val TAG_CLASS = "CLASS_API"
        private const val TAG_SKILL = "SKILL_API"
    }

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
        setContentView(R.layout.class_info_screen)

        loadHero("barbarian")
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

                    val allSkills = hero.skills.active + hero.skills.passive

                    populateSkillFragments(allSkills)
                }

                override fun onFailure(call: Call<HeroClassResponse>, t: Throwable) {
                    Log.e(TAG_CLASS, "API error for $slug", t)
                }
            })
    }

    private fun populateSkillFragments(skills: List<HeroSkill>) {
        val containerLayout = findViewById<LinearLayout>(R.id.skill_fragment_container)
            ?: run {
                Log.e(TAG_CLASS, "Skill container not found")
                return
            }

        skills.forEachIndexed { index, skill ->
            val bundle = bundleOf(
                "name" to skill.name,
                "cost" to skill.level,
                "costUnits" to "Fury",
                "desc" to skill.description
            )

            val fragmentContainer = androidx.fragment.app.FragmentContainerView(this).apply {
                id = View.generateViewId()
                layoutParams = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
                ).apply {
                    bottomMargin = 12
                }
            }

            containerLayout.addView(fragmentContainer)

            supportFragmentManager.commit {
                setReorderingAllowed(true)
                add<ClassAbility>(fragmentContainer.id, args = bundle)
            }
        }
    }
}
