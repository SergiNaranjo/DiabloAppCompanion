package com.example.d3grimoire.information

import API.DiabloApiInstance
import API.DiabloImageUrl
import API.model.HeroClassResponse
import API.model.HeroSkill
import android.os.Bundle
import android.util.Log
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import coil.load
import com.example.d3grimoire.R
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
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
    private lateinit var skillsAdapter: ClassAbilityAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_class_information);

        // RecyclerView keeps long skill lists efficient.
        val skillsRecycler: RecyclerView = findViewById(R.id.skills_recycler)
        skillsAdapter = ClassAbilityAdapter()
        skillsRecycler.layoutManager = LinearLayoutManager(this)
        skillsRecycler.adapter = skillsAdapter

        heroSlug = intent.getStringExtra(
            getString(R.string.class_information_hero_slug_key)) ?:
            getString(R.string.class_information_hero_slug_default);

        setupGenderButtons();
        loadPortrait();
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
        val portraitUrl: String = DiabloImageUrl.classPortrait(this, heroSlug, currentGender)
        Log.d("CLASS_PORTRAIT", "Class portrait URL: $portraitUrl")
        val portraitView: ImageView = findViewById<ImageView>(R.id.hero_portrait_image)
        portraitView.setImageResource(R.drawable.ic_template_classes)
        portraitView.background = null
        portraitView.load(portraitUrl) {
            crossfade(true)
            listener(
                onSuccess = { _, _ ->
                    Log.d("CLASS_PORTRAIT", "Class portrait loaded")
                },
                onError = { _, result ->
                    Log.e("CLASS_PORTRAIT", "Class portrait failed", result.throwable)
                    portraitView.setImageResource(R.drawable.ic_template_classes)
                }
            )
        }
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
        Log.d("CLASS_API", "Loading class data for slug=$slug")
        DiabloApiInstance.api.getHeroClass(slug)
            .enqueue(object : Callback<HeroClassResponse> {
                override fun onResponse(
                    call: Call<HeroClassResponse>,
                    response: Response<HeroClassResponse>
                ) {
                    if (!response.isSuccessful) {
                        Log.e("CLASS_API", "Class API failed: ${response.code()}")
                        return
                    }
                    val hero: HeroClassResponse = response.body() ?: return;

                    findViewById<TextView>(R.id.class_name_text).text = hero.name;
                    updateClassDescription(hero.slug);
                    loadPortrait();

                    val skills: List<HeroSkill> = hero.skills.active + hero.skills.passive;
                    skillsAdapter.submitList(skills)
                }

                override fun onFailure(call: Call<HeroClassResponse>, t: Throwable) {
                    Log.e("CLASS_API", "Failed to load $slug", t);
                }
            })
    }

    private fun updateClassDescription(slug: String) {
        val descriptionView: TextView = findViewById<TextView>(R.id.txtStrengthDesc);
        val primaryStat: String = when (slug) {
            getString(R.string.barbarian_slug),
            getString(R.string.crusader_slug) -> getString(R.string.primary_stat_strength)
            getString(R.string.demon_hunter_slug),
            getString(R.string.monk_slug) -> getString(R.string.primary_stat_dexterity)
            getString(R.string.necromancer_slug),
            getString(R.string.witch_doctor_slug),
            getString(R.string.wizard_slug) -> getString(R.string.primary_stat_intelligence)
            else -> getString(R.string.primary_stat_strength)
        }
        descriptionView.text = getString(R.string.class_primary_stat_format, primaryStat);
    }
}
