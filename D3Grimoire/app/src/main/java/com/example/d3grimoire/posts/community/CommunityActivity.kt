package com.example.d3grimoire.posts.community

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageButton
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import com.example.d3grimoire.NavBarActivity
import com.example.d3grimoire.R
import com.example.d3grimoire.posts.NewsData
import com.example.d3grimoire.posts.news.NewsPostButtonActivity
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.Query
import kotlin.math.min

class CommunityActivity : Fragment(R.layout.activity_community) {
    private lateinit var database: DatabaseReference
    private lateinit var view: View;

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        this.view = view;

        val imgBtn: ImageButton = view.findViewById<ImageButton>(R.id.new_post);
        imgBtn.setOnClickListener { newPost(); }

        database = FirebaseDatabase.getInstance(getString(R.string.database_URL))
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
        val act = requireActivity();
        if (act !is NavBarActivity) throw Exception("Invalid root node!");
        act.setFloatingButtonsVisibility(View.GONE);
        act.loadFragment(NewPostActivity());
    }

    private fun loadPosts(view: View) {
        val len: Int = min(
            communityNewsButtonIds.count(),
            communityNewsButtonData.count()
        );

        for (i in 0..len - 1) {
            val data: NewsData = communityNewsButtonData[i];
            val newsPostButton: NewsPostButtonActivity = NewsPostButtonActivity.newInstance(data);
            childFragmentManager.commit {
                setReorderingAllowed(true);
                add(data.id, newsPostButton);
            }
        }
    }

    companion object {
        public fun fetchPostData(database: DatabaseReference) {
            val query: Query = database.orderByKey();
            query.get()
                .addOnSuccessListener { snapshot ->
                    if (snapshot.exists()) {
                        var postIndex: Int = 0;
                        communityNewsButtonData.clear();
                        for (dataSnapshot in snapshot.children) {
                            Log.d("Community Screen", "Child");
                            if (postIndex >= communityNewsButtonIds.count()) break;
                            val newsData: NewsData = NewsData(
                                communityNewsButtonIds[postIndex],
                                dataSnapshot.child("title").getValue(String::class.java),
                                dataSnapshot.child("desc").getValue(String::class.java),
                                dataSnapshot.child("imgUrl").getValue(String::class.java),
                                dataSnapshot.child("url").getValue(String::class.java),
                                dataSnapshot.child("author").getValue(String::class.java)
                            );
                            communityNewsButtonData.add(newsData);
                            postIndex++;
                        }
                        Log.d("Community Screen", communityNewsButtonData.count().toString());
                    } else {
                        Log.d("Community Screen", "No posts")
                    }
                }
                .addOnFailureListener { exception ->
                    Log.e("Community Screen", "Error: ${exception.message}")
                }
        }
    }
}