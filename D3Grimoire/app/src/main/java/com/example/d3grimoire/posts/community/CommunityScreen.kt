package com.example.d3grimoire.posts.community

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.add
import androidx.fragment.app.commit
import com.example.d3grimoire.NavBar
import com.example.d3grimoire.R
import com.example.d3grimoire.posts.news.NewsScreen
import com.example.d3grimoire.posts.news.newsButtonData
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.Query

class CommunityScreen : Fragment(R.layout.community_screen) {

    private lateinit var database: DatabaseReference

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        val imgBtn: ImageButton = view.findViewById<ImageButton>(R.id.new_post);
        imgBtn.setOnClickListener { newPost(); }

        loadPosts(view);
    }

    private fun newPost() {
        val act = requireActivity()
        if (act !is NavBar) println("Bad Cast");
        else {
            act.setFloatingButtonsVisibility(View.GONE);
            act.loadFragment(NewPostActivity())
        }
    }

    private fun loadPosts(view: View) {
        val databaseUrl =
            "https://appcompanion-eedc3-default-rtdb.europe-west1.firebasedatabase.app/"
        database = FirebaseDatabase.getInstance(databaseUrl)
            .getReference("posts")

        val query: Query = database.orderByKey();
        query.get()
            .addOnSuccessListener { snapshot ->
                if(snapshot.exists()) {
                    var postIndex: Int = 0
                    for (dataSnapshot in snapshot.children) {
                        if(postIndex >= communityNewsButtonData.count()) return@addOnSuccessListener;
                        val bundle: Bundle = bundleOf(
                            "title" to dataSnapshot.child("title").getValue(String::class.java),
                            "desc" to dataSnapshot.child("desc").getValue(String::class.java),
                            "author" to "a"
                        );
                        childFragmentManager.commit {
                            setReorderingAllowed(true);
                            add<CommunityPost>(communityNewsButtonData[postIndex].id, args = bundle);
                        }
                        postIndex++;
                    }
                } else { println("No posts") }
            }
            .addOnFailureListener { exception ->
                println("Error: ${exception.message}")
            }
    }
}