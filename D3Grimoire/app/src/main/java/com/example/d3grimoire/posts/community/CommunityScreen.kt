package com.example.d3grimoire.posts.community

import android.os.Bundle
import android.view.View
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.add
import androidx.fragment.app.commit
import com.example.d3grimoire.R

class CommunityScreen : Fragment(R.layout.community_screen) {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        val imgBtn: ImageButton = view.findViewById<ImageButton>(R.id.new_post);
        imgBtn.setOnClickListener { newPost(); }

        var bundle : Bundle = bundleOf(
            "title" to "NEW MATERIAL FOR FARMING XP",
            "desc" to "A new advanced tutorial has been released...",
            "author" to "Posted by RazR0thy22X"
        );
        childFragmentManager.commit {
            setReorderingAllowed(true);
            add<CommunityPost>(R.id.fragment_container_view, args = bundle);
        }

        bundle = bundleOf(
            "title" to "TUTORIAL ON DEPRAVATO DIABLO",
            "desc" to "A complete advanced tutorial has been released...",
            "author" to "Posted by EnSparta II"
        );
        childFragmentManager.commit {
            setReorderingAllowed(true);
            add<CommunityPost>(R.id.post_2, args = bundle);
        }

        bundle = bundleOf(
            "title" to "NEW MATERIAL FOR FARMING XP",
            "desc" to "A new advanced tutorial has been released...",
            "author" to "Posted by RazR0thy22X"
        );
        childFragmentManager.commit {
            setReorderingAllowed(true);
            add<CommunityPost>(R.id.post_3, args = bundle);
        }
    }

    private fun newPost() {
        //TODO: change to new post screen
    }
}