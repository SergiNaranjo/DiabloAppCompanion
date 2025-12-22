package com.example.d3grimoire.posts.community

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.ImageButton
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import com.example.d3grimoire.NavBar
import com.example.d3grimoire.R
import com.example.d3grimoire.UserHandler
import com.example.d3grimoire.posts.news.NewsScreen
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import java.io.InputStream
import java.net.URL
import java.util.concurrent.Executor
import java.util.concurrent.Executors

class NewPostActivity : Fragment(R.layout.activity_new_post) {

    private lateinit var database: DatabaseReference;

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState);

        val uploadPostButton: ImageButton = view.findViewById<ImageButton>(R.id.upload_post);
        val exitButton: ImageButton = view.findViewById<ImageButton>(R.id.exit_new_post);

        database = FirebaseDatabase.getInstance(getString(R.string.database_URL))
            .getReference("posts");

        uploadPostButton.setOnClickListener {
            tryUploadPost(view);
        }

        exitButton.setOnClickListener {
            exitToNews();
        }
    }

    private fun getData(view: View): Map<String, String> {
        return mapOf(
            "title" to view.findViewById<EditText>(R.id.new_post_title).text.toString(),
            "desc" to view.findViewById<EditText>(R.id.new_post_description).text.toString(),
            "url" to view.findViewById<EditText>(R.id.new_post_url).text.toString(),
            "imgUrl" to view.findViewById<EditText>(R.id.new_post_img_url).text.toString()
        );
    }

    private fun showInvalidImgUrlAlert() {
        val builder = AlertDialog.Builder(requireActivity())

        builder.setMessage("The image URL is invalid!");
        builder.setTitle("");
        builder.setCancelable(false);
        builder.setPositiveButton("Ok") { dialog, which ->
            dialog.cancel();
        }

        val alertDialog = builder.create();
        alertDialog.show();
    }

    private fun tryUploadPost(view: View) {
        //Check image url
        val executor: Executor = Executors.newSingleThreadExecutor();
        var image: Bitmap?;
        val handler: Handler = Handler(Looper.getMainLooper());
        executor.execute {
            try {
                val url: String? =
                    view.findViewById<EditText>(R.id.new_post_img_url).text.toString();
                val `in`: InputStream = URL(url).openStream();
                image = BitmapFactory.decodeStream(`in`);
                if (image == null) throw Exception("Image is null!");

                pushPost(view);
                exitToNews();
            } catch (e: Exception) {
                handler.post {
                    showInvalidImgUrlAlert();
                }
                e.printStackTrace();
            }
        }
    }

    private fun pushPost(view: View) {
        val dataId = database.push().key;
        dataId?.let {
            database.child(dataId)
                .setValue(getData(view))
                .addOnSuccessListener {
                    postAnalyticsPostCreator();
                }
                .addOnFailureListener {
                    Log.e("Firebase", "Write failed", it)
                };
        }
    }

    private fun postAnalyticsPostCreator() {
        val act = requireActivity();
        if (act !is AppCompatActivity) return;
        var user: String?;
        if (UserHandler.getUserGoogle(act) != null) {
            user = UserHandler.getUserGoogle(act)!!.displayName;
        } else if (UserHandler.getUserNative(act) != null) {
            user = UserHandler.getUserNative(act);
        } else {
            user = "";
        }
        val bundle: Bundle = bundleOf(
            "post_creation_user" to user
        );
        FirebaseAnalytics.getInstance(requireActivity())
            .logEvent("NewPost", bundle);
    }

    private fun exitToNews() {
        val activity: FragmentActivity = requireActivity();
        if (activity !is NavBar) throw Exception("Invalid root node!");
        activity.setFloatingButtonsVisibility(View.VISIBLE);
        activity.loadFragment(NewsScreen());
    }
}