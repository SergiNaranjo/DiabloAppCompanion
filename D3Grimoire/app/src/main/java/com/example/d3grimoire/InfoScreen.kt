package com.example.d3grimoire

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.bundleOf
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.add
import androidx.fragment.app.commit

class InfoScreen : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.info_screen);

        var bundle : Bundle = bundleOf(
            "name" to "THE BUTCHER'S CLEAVER",
            "type" to "MAGIC AXE",
            "requiredLevel" to 3,
            "desc" to "118.3 DAMAGE PER SECOND\n+30 STRENGTH\n+20 ATTACKS PER SECOND"
        );
        supportFragmentManager.commit {
            setReorderingAllowed(true);
            add<Item>(R.id.item_1, args = bundle);
        }

        bundle = bundleOf(
            "name" to "AIDAN'S REVENGE",
            "type" to "AXE",
            "requiredLevel" to 1,
            "desc" to "10.4 DAMAGE PER SECOND\n+10 STRENGTH\n+10 ATTACKS PER SECOND"
        );
        supportFragmentManager.commit {
            setReorderingAllowed(true);
            add<Item>(R.id.item_2, args = bundle);
        }
    }
}