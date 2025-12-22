package com.example.d3grimoire

import API.DiabloApiInstance
import API.DiabloImageUrl
import API.model.HeroClassResponse
import API.model.HeroSkill
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.bundleOf
import androidx.fragment.app.add
import androidx.fragment.app.commit
import com.bumptech.glide.Glide
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ClassInfoScreen : AppCompatActivity() {

    private var currentGender = "male"
    private lateinit var heroSlug: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.class_info_screen)

        heroSlug = intent.getStringExtra("HERO_SLUG") ?: "barbarian"

        setupGenderButtons()
        loadHero(heroSlug)
        loadClassGif(heroSlug)
    }

    private fun setupGenderButtons() {
        val maleBtn = findViewById<ImageButton>(R.id.circle1)
        val femaleBtn = findViewById<ImageButton>(R.id.circle2)

        maleBtn.setOnClickListener {
            currentGender = "male"
            updateGenderUI(maleBtn, femaleBtn)
        }

        femaleBtn.setOnClickListener {
            currentGender = "female"
            updateGenderUI(maleBtn, femaleBtn)
        }
    }

    private fun updateGenderUI(maleBtn: ImageButton, femaleBtn: ImageButton) {
        if (currentGender == "male") {
            maleBtn.setBackgroundResource(R.drawable.ic_btn_male_active)
            femaleBtn.setBackgroundResource(R.drawable.ic_btn_female_deactive)
        } else {
            maleBtn.setBackgroundResource(R.drawable.ic_btn_male_deactive)
            femaleBtn.setBackgroundResource(R.drawable.ic_btn_female_active)
        }
        loadPortrait()
    }

    private fun loadPortrait() {
        val portraitView = findViewById<ImageView>(R.id.imgHeroPortrait)

        val url = DiabloImageUrl.classPortrait(heroSlug, currentGender)
        Log.d("PORTRAIT", "Loading: $url")

        Glide.with(this)
            .load(url)
            .placeholder(R.drawable.ic_template_classes)
            .error(R.drawable.ic_template_classes)
            .into(portraitView)
    }

    private fun loadClassGif(slug: String) {
        val gifView = findViewById<ImageView>(R.id.imgClassGifBg)

        Glide.with(this)
            .asGif()
            .load(DiabloImageUrl.classGif(slug))
            .centerCrop()
            .into(gifView)
    }

    private fun loadHero(slug: String) {
        DiabloApiInstance.api.getHeroClass(slug)
            .enqueue(object : Callback<HeroClassResponse> {
                override fun onResponse(
                    call: Call<HeroClassResponse>,
                    response: Response<HeroClassResponse>
                ) {
                    val hero = response.body() ?: return

                    findViewById<TextView>(R.id.txtBarbarianTitle).text = hero.name

                    loadPortrait()

                    val skills = hero.skills.active + hero.skills.passive
                    populateSkillFragments(skills)
                }

                override fun onFailure(call: Call<HeroClassResponse>, t: Throwable) {
                    Log.e("CLASS_API", "Failed to load $slug", t)
                }
            })
    }

    private fun populateSkillFragments(skills: List<HeroSkill>) {
        val container = findViewById<LinearLayout>(R.id.skill_fragment_container)
        container.removeAllViews()

        skills.forEach { skill ->
            val bundle = bundleOf(
                "name" to skill.name,
                "level" to skill.level,
                "cost" to 0,
                "costUnits" to "Resource",
                "desc" to skill.description,
                "icon" to skill.icon
            )

            val fragmentContainer = androidx.fragment.app.FragmentContainerView(this).apply {
                id = View.generateViewId()
                layoutParams = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
                ).apply {
                    bottomMargin = 24
                }
            }

            container.addView(fragmentContainer)

            supportFragmentManager.commit {
                setReorderingAllowed(true)
                add<ClassAbility>(fragmentContainer.id, args = bundle)
            }
        }
    }
}
