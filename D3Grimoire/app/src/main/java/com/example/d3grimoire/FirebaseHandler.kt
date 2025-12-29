package com.example.d3grimoire

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.core.os.bundleOf
import com.example.d3grimoire.posts.news.NewsPostButtonActivity
import com.example.d3grimoire.signin.UserHandler
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class FirebaseHandler {
    companion object {
        public lateinit var database: FirebaseDatabase;
        public lateinit var usersReference: DatabaseReference;
        public lateinit var postsReference: DatabaseReference;

        public fun Init(context: Context) {
            database = FirebaseDatabase.getInstance(context.getString(R.string.database_URL));
            usersReference = database.getReference(
                context.getString(R.string.firebase_users_reference_path)
            );
            postsReference = database.getReference(
                context.getString(R.string.firebase_posts_reference_path)
            );
        }

        public fun analyticsLogPostSelected(context: Context, post: NewsPostButtonActivity) {
            val bundle: Bundle = bundleOf(
                context.getString(R.string.firebase_analytics_post_click_user_key)
                        to UserHandler.getUsername(context),
                context.getString(R.string.firebase_analytics_post_click_author_key)
                        to post.requireArguments().getString("author")
            );
            FirebaseAnalytics.getInstance(context)
                .logEvent(
                    context.getString(
                        R.string.firebase_analytics_post_selected_event
                    ),
                    bundle
                );
        }

        public fun analyticsLogPostCreated(context: Context) {
            val bundle: Bundle = bundleOf(
                context.getString(R.string.firebase_analytics_post_creation_user_key)
                        to UserHandler.getUsername(context)
            );
            FirebaseAnalytics.getInstance(context)
                .logEvent(
                    context.getString(R.string.firebase_analytics_post_created_event),
                    bundle
                );
        }

        public fun pushPost(context: Context, postInformation: Map<String, String>) {
            val dataId: String? = postsReference.push().key;
            dataId?.let {
                postsReference.child(dataId)
                    .setValue(postInformation)
                    .addOnSuccessListener {
                        analyticsLogPostCreated(context);
                    }
                    .addOnFailureListener {
                        Log.e(
                            context.getString(R.string.firebase_error_tag),
                            "Write failed",
                            it
                        );
                    };
            }
        }
    }
}