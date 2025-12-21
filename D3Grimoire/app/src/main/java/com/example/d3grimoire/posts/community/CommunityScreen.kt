package com.example.d3grimoire.posts.community

import android.content.Intent
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
import com.example.d3grimoire.posts.NewsData
import com.example.d3grimoire.posts.news.NewsPost
import com.example.d3grimoire.posts.news.NewsPostButton
import com.example.d3grimoire.posts.news.NewsScreen
import com.example.d3grimoire.posts.news.newsButtonData
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.Query
import kotlin.math.min

class CommunityScreen : Fragment(R.layout.community_screen) {

    private lateinit var database: DatabaseReference
    private lateinit var view: View;

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        this.view = view;

        val imgBtn: ImageButton = view.findViewById<ImageButton>(R.id.new_post);
        imgBtn.setOnClickListener { newPost(); }

        val databaseUrl =
            "https://appcompanion-eedc3-default-rtdb.europe-west1.firebasedatabase.app/";
        database = FirebaseDatabase.getInstance(databaseUrl)
            .getReference("posts");

        database.addChildEventListener(createChildEventListener());

        loadPosts(view);
    }

    private fun createChildEventListener(): ChildEventListener {
        return object : ChildEventListener {
            override fun onChildAdded(
                snapshot: DataSnapshot,
                previousChildName: String?
            ) {
                fetchPostData(database);
            }

            override fun onChildChanged(
                snapshot: DataSnapshot,
                previousChildName: String?
            ) {
                fetchPostData(database);
            }

            override fun onChildRemoved(snapshot: DataSnapshot) {
                fetchPostData(database);
            }

            override fun onChildMoved(
                snapshot: DataSnapshot,
                previousChildName: String?
            ) {
                fetchPostData(database);
            }

            override fun onCancelled(error: DatabaseError) {
                Log.d("Firebase", "Cancelled - Error: ${error.message}");
            }

        }
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
        val len: Int = min(
            communityNewsButtonIds.count(),
            communityNewsButtonData.count()
        );
        Log.d("Community Posts", len.toString());
        for(i in 0..len-1) {
            val data: NewsData = communityNewsButtonData[i];

            val intent : Intent = Intent(requireActivity(), NewsPost::class.java);
            intent.putExtra("url", data.url);

            val newsPostButton: NewsPostButton = NewsPostButton.newInstance(
                data,
                {
                    requireActivity().run {
                        startActivity(intent);
                    }
                }
            );

            childFragmentManager.commit {
                setReorderingAllowed(true);
                add(data.id, newsPostButton);
            }
        }
    }
}