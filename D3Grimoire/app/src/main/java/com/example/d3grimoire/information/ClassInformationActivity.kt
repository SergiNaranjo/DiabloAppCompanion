package com.example.d3grimoire.information

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
import androidx.fragment.app.FragmentContainerView
import androidx.fragment.app.add
import androidx.fragment.app.commit
import com.bumptech.glide.Glide
import com.example.d3grimoire.R
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ClassInformationActivity : AppCompatActivity() {

    enum class Gender {
        MALE,
        FEMALE
    }

    private lateinit var maleGenderButton: ImageButton;
    private lateinit var femaleGenderButton: ImageButton;
    private var currentGender: Gender = Gender.MALE;
    private lateinit var heroSlug: String;
    private val SKILL_BOTTOM_MARGIN: Int = 24;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_class_information);

        heroSlug = intent.getStringExtra(
            getString(R.string.class_information_hero_slug_key)) ?:
            getString(R.string.class_information_hero_slug_default);

        setupGenderButtons();
        loadHero(heroSlug);
        loadClassGif(heroSlug);
    }

    private fun setupGenderButtons() {
        maleGenderButton = findViewById<ImageButton>(R.id.male_button);
        femaleGenderButton = findViewById<ImageButton>(R.id.female_button);

        maleGenderButton.setOnClickListener {
            updateGenderUI(Gender.MALE);
        }

        femaleGenderButton.setOnClickListener {
            updateGenderUI(Gender.FEMALE);
        }
    }

    private fun updateGenderUI(newGender: Gender) {
        if(currentGender == newGender) return;
        currentGender = newGender;

        when (currentGender) {
            Gender.MALE -> {
                maleGenderButton.setBackgroundResource(R.drawable.ic_btn_male_active);
                femaleGenderButton.setBackgroundResource(R.drawable.ic_btn_female_inactive);
            }

            Gender.FEMALE -> {
                maleGenderButton.setBackgroundResource(R.drawable.ic_btn_male_inactive);
                femaleGenderButton.setBackgroundResource(R.drawable.ic_btn_female_active);
            }
        }

        loadPortrait();
    }

    private fun loadPortrait() {
        val portraitView: ImageView = findViewById<ImageView>(R.id.hero_portrait_image);

        val url: String = DiabloImageUrl.classPortrait(this, heroSlug, currentGender);

        Glide.with(this)
            .load(url)
            .placeholder(R.drawable.ic_template_classes)
            .error(R.drawable.ic_template_classes)
            .into(portraitView);
    }

    private fun loadClassGif(slug: String) {
        val gifView: ImageView = findViewById<ImageView>(R.id.class_gif_background_image);

        Glide.with(this)
            .asGif()
            .load(DiabloImageUrl.classGif(this, slug))
            .centerCrop()
            .into(gifView);
    }

    private fun loadHero(slug: String) {
        DiabloApiInstance.api.getHeroClass(slug)
            .enqueue(object : Callback<HeroClassResponse> {
                override fun onResponse(
                    call: Call<HeroClassResponse>,
                    response: Response<HeroClassResponse>
                ) {
                    val hero: HeroClassResponse = response.body() ?: return;

                    findViewById<TextView>(R.id.class_name_text).text = hero.name;

                    loadPortrait();

                    val skills: List<HeroSkill> = hero.skills.active + hero.skills.passive;
                    populateSkillFragments(skills);
                }

                override fun onFailure(call: Call<HeroClassResponse>, t: Throwable) {
                    Log.e("CLASS_API", "Failed to load $slug", t);
                }
            })
    }

    private fun populateSkillFragments(skills: List<HeroSkill>) {
        val container: LinearLayout = findViewById<LinearLayout>(R.id.skill_fragment_container);
        container.removeAllViews();

        skills.forEach { skill: HeroSkill ->
            val bundle = bundleOf(
                getString(R.string.class_ability_name_key) to skill.name,
                getString(R.string.class_ability_level_key) to skill.level,
                getString(R.string.class_ability_cost_key) to 0,
                getString(R.string.class_ability_costunits_key) to getString(R.string.class_ability_cost_default),
                getString(R.string.class_ability_description_key) to skill.description,
                getString(R.string.class_ability_icon_key) to skill.icon
            );

            val fragmentContainer: FragmentContainerView = FragmentContainerView(this).apply {
                id = View.generateViewId();
                layoutParams = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
                ).apply {
                    bottomMargin = SKILL_BOTTOM_MARGIN;
                };
            }

            container.addView(fragmentContainer);

            supportFragmentManager.commit {
                setReorderingAllowed(true);
                add<ClassAbilityActivity>(fragmentContainer.id, args = bundle);
            }
        }
    }
}
