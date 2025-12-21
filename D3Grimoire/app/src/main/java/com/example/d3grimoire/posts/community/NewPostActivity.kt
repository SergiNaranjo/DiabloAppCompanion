package com.example.d3grimoire.posts.community

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.ImageButton
import androidx.fragment.app.Fragment
import com.example.d3grimoire.NavBar
import com.example.d3grimoire.R
import com.example.d3grimoire.posts.news.NewsScreen
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class NewPostActivity : Fragment(R.layout.activity_new_post) {

    private lateinit var database: DatabaseReference

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val uploadPostButton: ImageButton = view.findViewById<ImageButton>(R.id.upload_post);
        val exitButton: ImageButton = view.findViewById<ImageButton>(R.id.exit_new_post);

        val databaseUrl =
            "https://appcompanion-eedc3-default-rtdb.europe-west1.firebasedatabase.app/"
        database = FirebaseDatabase.getInstance(databaseUrl)
            .getReference("posts")

        uploadPostButton.setOnClickListener {

            val dataId = database.push().key

            val messageData = mapOf(
                "title" to view.findViewById<EditText>(R.id.new_post_title).text.toString(),
                "desc" to view.findViewById<EditText>(R.id.new_post_description).text.toString(),
                "url" to view.findViewById<EditText>(R.id.new_post_url).text.toString(),
                "imgUrl" to view.findViewById<EditText>(R.id.new_post_img_url).text.toString()
            )

            dataId?.let {
                database.child(dataId)
                    .setValue(messageData)
                    .addOnSuccessListener {
                        Log.d("FIREBASE", "Message written")
                    }
                    .addOnFailureListener {
                        Log.e("FIREBASE", "Write failed", it)
                    }
            }
        }

        exitButton.setOnClickListener {
            val act = requireActivity()
            if (act !is NavBar) println("Bad Cast");
            else {
                act.setFloatingButtonsVisibility(View.VISIBLE);
                act.loadFragment(NewsScreen());
            }
        }
    }
}