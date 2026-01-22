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
import com.example.d3grimoire.FirebaseHandler
import com.example.d3grimoire.NavBarActivity
import com.example.d3grimoire.R
import com.example.d3grimoire.Utils
import com.example.d3grimoire.signin.UserHandler
import com.example.d3grimoire.posts.news.NewsScreen
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import java.io.InputStream
import java.net.URL
import java.util.concurrent.Executor
import java.util.concurrent.Executors

class NewPostActivity : Fragment(R.layout.activity_new_post) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState);

        val uploadPostButton: ImageButton = view.findViewById<ImageButton>(R.id.upload_post);
        val exitButton: ImageButton = view.findViewById<ImageButton>(R.id.exit_new_post);

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
        val builder: AlertDialog.Builder = AlertDialog.Builder(requireActivity())

        builder.setMessage("The image URL is invalid!");
        builder.setTitle("");
        builder.setCancelable(false);
        builder.setPositiveButton("Ok") { dialog, which ->
            dialog.cancel();
        }

        val alertDialog: AlertDialog = builder.create();
        alertDialog.show();
    }

    private fun tryUploadPost(view: View) {
        // Validate image URLs off the main thread before posting.
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

                FirebaseHandler.pushPost(requireActivity(), getData(view));
                exitToNews();
            } catch (e: Exception) {
                handler.post {
                    showInvalidImgUrlAlert();
                }
                e.printStackTrace();
            }
        }
    }



    private fun exitToNews() {
        Utils.getNavBarFromFragment(this).setFloatingButtonsVisibility(View.VISIBLE);
        Utils.getNavBarFromFragment(this).loadFragment(CommunityActivity());
    }
}
