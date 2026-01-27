package com.example.d3grimoire.posts.community

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.EditText
import android.widget.ImageButton
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import com.example.d3grimoire.FirebaseHandler
import com.example.d3grimoire.R
import com.example.d3grimoire.ActivityCaster
import com.example.d3grimoire.ImageDecoder
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
        // Validate image url and post
        val executor: Executor = Executors.newSingleThreadExecutor();
        val handler: Handler = Handler(Looper.getMainLooper());
        executor.execute {
            val url: String? =
                view.findViewById<EditText>(R.id.new_post_img_url).text.toString();
            if (!ImageDecoder.isImageURLValid(url)) {
                handler.post {
                    showInvalidImgUrlAlert();
                }
            } else {
                try {
                    FirebaseHandler.pushPost(requireActivity(), getData(view));
                    exitToNews();
                } catch (e: Exception) {
                    e.printStackTrace();
                }
            }
        }
    }


    private fun exitToNews() {
        ActivityCaster.getNavBarFromFragment(this).setFloatingButtonsVisibility(View.VISIBLE);
        ActivityCaster.getNavBarFromFragment(this).loadFragment(CommunityActivity());
    }
}
