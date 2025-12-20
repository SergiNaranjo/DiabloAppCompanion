package com.example.d3grimoire

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.bundleOf
import androidx.fragment.app.add
import androidx.fragment.app.commit

class ClassInfoScreen : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.class_info_screen)

        var bundle : Bundle = bundleOf(
            "name" to "WHIRLWIND",
            "cost" to 10,
            "costUnits" to "Fury",
            "desc" to "Unleash a flurry of attacks hitting everything in your path for 100% damage."
        );
        supportFragmentManager.commit {
            setReorderingAllowed(true);
            add<ClassAbility>(R.id.class_ability_1, args = bundle);
        }

        bundle = bundleOf(
            "name" to "HAMMER OF THE ANCIENTS",
            "cost" to 25,
            "costUnits" to "Fury",
            "desc" to "Smash the ground with an ancient hammer for massive concentrated damage."
        );
        supportFragmentManager.commit {
            setReorderingAllowed(true);
            add<ClassAbility>(R.id.class_ability_2, args = bundle);
        }

        bundle = bundleOf(
            "name" to "WHIRLWIND",
            "cost" to 10,
            "costUnits" to "Fury",
            "desc" to "While whirling you move at +100% increased speed."
        );
        supportFragmentManager.commit {
            setReorderingAllowed(true);
            add<ClassAbility>(R.id.class_ability_3, args = bundle);
        }
    }
}