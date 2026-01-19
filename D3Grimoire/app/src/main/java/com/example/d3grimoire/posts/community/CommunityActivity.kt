package com.example.d3grimoire.posts.community

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageButton
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.d3grimoire.FirebaseHandler
import com.example.d3grimoire.NavBarActivity
import com.example.d3grimoire.R
import com.example.d3grimoire.posts.NewsData
import com.example.d3grimoire.posts.NewsPostAdapter
import com.example.d3grimoire.posts.news.NewsPostActivity
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.Query
import android.content.Intent

class CommunityActivity : Fragment(R.layout.activity_community) {
    private lateinit var adapter: NewsPostAdapter

    companion object {
        private const val POST_KEY_TITLE: String = "title"
        private const val POST_KEY_DESC: String = "desc"
        private const val POST_KEY_IMG_URL: String = "imgUrl"
        private const val POST_KEY_URL: String = "url"
        private const val POST_KEY_AUTHOR: String = "author"
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        val recyclerView: RecyclerView = view.findViewById<RecyclerView>(R.id.community_posts_recycler)
        adapter = NewsPostAdapter { data -> openPost(data) }
        recyclerView.layoutManager = LinearLayoutManager(view.context)
        recyclerView.adapter = adapter

        val imgBtn: ImageButton = view.findViewById<ImageButton>(R.id.new_post);
        imgBtn.setOnClickListener { newPost(); }

        FirebaseHandler.postsReference.addChildEventListener(createChildEventListener());

        fetchPostData();
    }

    private fun createChildEventListener(): ChildEventListener {
        return object : ChildEventListener {
            override fun onChildAdded(
                snapshot: DataSnapshot,
                previousChildName: String?
            ) {
                fetchPostData();
            }

            override fun onChildChanged(
                snapshot: DataSnapshot,
                previousChildName: String?
            ) {
                fetchPostData();
            }

            override fun onChildRemoved(snapshot: DataSnapshot) {
                fetchPostData();
            }

            override fun onChildMoved(
                snapshot: DataSnapshot,
                previousChildName: String?
            ) {
                fetchPostData();
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("Firebase", "Cancelled - Error: ${error.message}");
            }

        }
    }

    private fun newPost() {
        val act = requireActivity();
        if (act !is NavBarActivity) throw Exception("Invalid root node!");
        act.setFloatingButtonsVisibility(View.GONE);
        act.loadFragment(NewPostActivity());
    }

    private fun fetchPostData() {
        val query: Query = FirebaseHandler.postsReference.orderByKey();
        query.get()
            .addOnSuccessListener { snapshot ->
                if (snapshot.exists()) {
                    var postIndex: Int = 0;
                    communityNewsButtonData.clear();
                    for (dataSnapshot in snapshot.children) {
                        val newsData: NewsData = NewsData(
                            postIndex,
                            dataSnapshot.child(POST_KEY_TITLE).getValue(String::class.java),
                            dataSnapshot.child(POST_KEY_DESC).getValue(String::class.java),
                            dataSnapshot.child(POST_KEY_IMG_URL).getValue(String::class.java),
                            dataSnapshot.child(POST_KEY_URL).getValue(String::class.java),
                            dataSnapshot.child(POST_KEY_AUTHOR).getValue(String::class.java)
                        );
                        communityNewsButtonData.add(newsData);
                        postIndex++;
                    }
                    // Refresh the list with the latest snapshot contents.
                    adapter.submitList(communityNewsButtonData.toList());
                } else {
                    adapter.submitList(emptyList());
                    Log.d("Community Screen", "No posts")
                }
            }
            .addOnFailureListener { exception ->
                Log.e("Community Screen", "Error: ${exception.message}")
            }
    }

    private fun openPost(data: NewsData) {
        FirebaseHandler.analyticsLogPostSelected(requireActivity(), data);
        val intent: Intent = Intent(requireActivity(), NewsPostActivity::class.java);
        intent.putExtra(NewsPostActivity.EXTRA_URL, data.url);
        startActivity(intent);
    }
}
