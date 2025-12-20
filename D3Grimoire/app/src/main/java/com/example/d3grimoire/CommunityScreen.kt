package com.example.d3grimoire

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.bundleOf
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.add
import androidx.fragment.app.commit

class CommunityScreen : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.community_screen)
        var bundle : Bundle = bundleOf(
            "title" to "NEW MATERIAL FOR FARMING XP",
            "desc" to "A new advanced tutorial has been released...",
            "author" to "Posted by RazR0thy22X"
        );
        supportFragmentManager.commit {
            setReorderingAllowed(true);
            add<CommunityPost>(R.id.fragment_container_view, args = bundle);
        }

        bundle = bundleOf(
            "title" to "TUTORIAL ON DEPRAVATO DIABLO",
            "desc" to "A complete advanced tutorial has been released...",
            "author" to "Posted by EnSparta II"
        );
        supportFragmentManager.commit {
            setReorderingAllowed(true);
            add<CommunityPost>(R.id.post_2, args = bundle);
        }

        bundle = bundleOf(
            "title" to "NEW MATERIAL FOR FARMING XP",
            "desc" to "A new advanced tutorial has been released...",
            "author" to "Posted by RazR0thy22X"
        );
        supportFragmentManager.commit {
            setReorderingAllowed(true);
            add<CommunityPost>(R.id.post_3, args = bundle);
        }
    }
}