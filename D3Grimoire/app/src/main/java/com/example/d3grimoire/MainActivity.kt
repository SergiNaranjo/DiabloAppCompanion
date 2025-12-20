package com.example.d3grimoire

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.bundleOf
import androidx.fragment.app.add
import androidx.fragment.app.commit
import com.google.firebase.Firebase
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.analytics
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class MainActivity : AppCompatActivity() {

    private lateinit var analytics: FirebaseAnalytics;
    private lateinit var database: DatabaseReference;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.splash_screen)

        analytics = Firebase.analytics;
        analytics.logEvent("Help", null);

        val databaseUrl = "https://appcompanion-eedc3-default-rtdb.europe-west1.firebasedatabase.app/";
        database = FirebaseDatabase.getInstance(databaseUrl).getReference("messages");

        val dataId = database.push().key;

        val messageData = mapOf(
            "user" to "Jose",
            "message" to "Hello World"

        );

        if(dataId!= null) {
            database.child(dataId).setValue(messageData)
                .addOnSuccessListener { result ->
                    println("success!");
                }
                .addOnFailureListener {
                    println("failure :(");
                }
        }

        startActivity(Intent(this, NavBar::class.java));
    }
}