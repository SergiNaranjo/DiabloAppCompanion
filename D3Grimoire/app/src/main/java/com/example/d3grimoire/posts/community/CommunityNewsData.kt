package com.example.d3grimoire.posts.community

import android.util.Log
import com.example.d3grimoire.R
import com.example.d3grimoire.posts.NewsData
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.Query

public val communityNewsButtonIds: List<Int> = listOf(
    R.id.post_1,
    R.id.post_2,
    R.id.post_3
);
public var communityNewsButtonData: MutableList<NewsData> = mutableListOf<NewsData>();

public fun fetchPostData(database: DatabaseReference) {
    val query: Query = database.orderByKey();
    query.get()
        .addOnSuccessListener { snapshot ->
            if(snapshot.exists()) {
                var postIndex: Int = 0;
                communityNewsButtonData.clear();
                for (dataSnapshot in snapshot.children) {
                    Log.d("Community Screen", "Child");
                    if(postIndex >= communityNewsButtonIds.count()) break;
                    val newsData: NewsData = NewsData(
                        communityNewsButtonIds[postIndex],
                        dataSnapshot.child("title").getValue(String::class.java),
                        dataSnapshot.child("desc").getValue(String::class.java),
                        dataSnapshot.child("imgUrl").getValue(String::class.java),
                        dataSnapshot.child("url").getValue(String::class.java)
                    );
                    communityNewsButtonData.add(newsData);
                    postIndex++;
                }
                Log.d("Community Screen", communityNewsButtonData.count().toString());
            } else {
                println("No posts")
            }
        }
        .addOnFailureListener { exception ->
            println("Error: ${exception.message}")
        }
}