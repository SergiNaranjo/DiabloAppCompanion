package com.example.d3grimoire

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.InputType
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.add
import androidx.fragment.app.commit
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.Query
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.io.InputStream
import java.net.URL
import java.util.concurrent.Executor
import java.util.concurrent.Executors

class ProfileActivity : Fragment(R.layout.activity_profile) {

    private lateinit var database: DatabaseReference;
    private lateinit var imageUrlEditText: EditText;
    private lateinit var statusEditText: EditText;
    private var editState: Boolean = false;

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val activity: FragmentActivity = requireActivity();
        if(activity !is NavBarActivity) throw Exception("Invalid root activity!");
        if(!UserHandler.isSignedIn(activity)) {
            activity.loadFragment(SignInActivity());
        }

        database = FirebaseDatabase.getInstance(getString(R.string.database_URL))
            .getReference("users");

        val signOutButton: TextView = view.findViewById<TextView>(R.id.btn_sign_out);
        signOutButton.setOnClickListener { signOut(); };

        imageUrlEditText = view.findViewById<EditText>(R.id.profile_img_url);
        statusEditText = view.findViewById<EditText>(R.id.profile_status);

        setEditState(editState);

        loadUsername(view);
        loadStatus(view);

        val editButton: ImageButton = view.findViewById<ImageButton>(R.id.profile_edit_btn);
        editButton.setOnClickListener { editProfile(view); };

        loadProfilePicture(view);

        makeHeroData();

        for(i in heroIds.indices) {
            childFragmentManager.commit {
                setReorderingAllowed(true);
                add<ProfileHeroActivity>(heroIds[i], args = heroData[i]);
            }
        }
    }

    private fun makeHeroData() {
        heroData = listOf(
            bundleOf(
                "name" to getString(R.string.hero_1_name),
                "class" to getString(R.string.hero_1_class),
                "level" to getString(R.string.hero_1_level)
            ),
            bundleOf(
                "name" to getString(R.string.hero_2_name),
                "class" to getString(R.string.hero_2_class),
                "level" to getString(R.string.hero_2_level)
            ),
            bundleOf(
                "name" to getString(R.string.hero_3_name),
                "class" to getString(R.string.hero_3_class),
                "level" to getString(R.string.hero_3_level)
            )
        );
    }

    private fun loadProfilePicture(view: View) {
        val activity: FragmentActivity = requireActivity();
        if(activity !is AppCompatActivity) throw Exception("Invalid root node!");

        var imgUrl: String? = null;

        val query: Query = database.orderByChild("user").equalTo(UserHandler.getUsername(activity));
        query.get()
            .addOnSuccessListener { snapshot ->
                if(snapshot.exists()) {
                    for(dataSnapshot in snapshot.children) {
                        imgUrl = dataSnapshot.child("imgUrl").getValue(String::class.java);
                    }
                }
                val imageView = view.findViewById<ImageView>(R.id.profile_picture)
                val executor = Executors.newSingleThreadExecutor()
                val handler = Handler(Looper.getMainLooper())
                var image: Bitmap?;

                executor.execute {
                    try {
                        val `in` = URL(imgUrl).openStream()
                        image = BitmapFactory.decodeStream(`in`)
                        handler.post {
                            imageView.setImageBitmap(image)
                        }
                    }
                    catch (e: Exception) {
                        e.printStackTrace()
                    }
                }
            }
            .addOnFailureListener { exception ->
                val message: String? = exception.message;
                message?.let { Log.e("Profile", message) }
            }
    }

    private fun editProfile(view: View) {
        if(!editState) {
            setEditState(true);
            return;
        }

        //Check image url
        val executor: Executor = Executors.newSingleThreadExecutor();
        var image: Bitmap?;
        executor.execute {
            try {
                val url: String? =
                    view.findViewById<EditText>(R.id.profile_img_url).text.toString();
                val `in`: InputStream = URL(url).openStream();
                image = BitmapFactory.decodeStream(`in`);
                if (image == null) throw Exception("Image is null!");

                pushEdit(view);

                //Small delay for the pfp to upload
                MainScope().launch {
                    delay(500);

                    val act: FragmentActivity = requireActivity();
                    if(act is NavBarActivity) {
                        act.loadFragment(ProfileActivity());
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace();
            }
        }
    }

    private fun pushEdit(view: View) {
        val act: FragmentActivity = requireActivity();
        if(act !is AppCompatActivity) throw Exception("Invalid root activity!");
        val username: String? = UserHandler.getUsername(act);
        val query: Query = database.orderByChild("user").equalTo(username);
        query.get()
            .addOnSuccessListener { snapshot ->
                if(!snapshot.exists()) return@addOnSuccessListener;

                val imgUrl: String = imageUrlEditText.text.toString();
                val status: String = statusEditText.text.toString();

                for(dataSnapshot in snapshot.children) {
                    val key: String? = dataSnapshot.key;
                    key?.let {
                        Log.d("Profile", key);
                        database.child(key).setValue(mapOf(
                            "user" to UserHandler.getUsername(act),
                            "password" to UserHandler.getPassNative(act),
                            "imgUrl" to imgUrl,
                            "status" to status
                        ));
                    }
                }
            }
            .addOnFailureListener { exception ->
                val message: String? = exception.message;
                message?.let { Log.e("Profile", message) }
            }
    }

    private fun signOut() {
        val activity: FragmentActivity = requireActivity();
        if(activity !is NavBarActivity) throw Exception("Invalid root activity!");
        UserHandler.signOutGoogle(activity);
        UserHandler.signOutNative(activity);
        activity.loadFragment(ProfileActivity());
    }

    private fun loadUsername(view: View) {
        val usernameText: TextView = view.findViewById<TextView>(R.id.profile_username);
        val act: FragmentActivity = requireActivity();
        if(act !is AppCompatActivity) throw Exception("Invalid root activity!");
        val username: String? = UserHandler.getUsername(act);
        username?.let{ usernameText.text = username; } ?: run { usernameText.text = "Not Signed in"; }
    }

    private fun loadStatus(view: View) {
        val activity: FragmentActivity = requireActivity();
        if(activity !is AppCompatActivity) throw Exception("Invalid root node!");

        var status: String? = null;

        val query: Query = database.orderByChild("user").equalTo(UserHandler.getUsername(activity));
        query.get()
            .addOnSuccessListener { snapshot ->
                if(snapshot.exists()) {
                    for (dataSnapshot in snapshot.children) {
                        status = dataSnapshot.child("status").getValue(String::class.java);
                    }
                }
                status?.let{
                    statusEditText.text = Editable.Factory.getInstance().newEditable(status);
                } ?: run {
                    statusEditText.text = Editable.Factory.getInstance().newEditable("Status here");
                }
            }
            .addOnFailureListener { exception ->
                val message: String? = exception.message;
                message?.let { Log.e("Profile", message) }
            }
    }

    private fun setEditState(state: Boolean) {
        editState = state;
        imageUrlEditText.visibility = if (state) View.VISIBLE else View.GONE;

        statusEditText.isFocusable = state;
        statusEditText.isFocusableInTouchMode = state;
        statusEditText.inputType = if (state) InputType.TYPE_CLASS_TEXT else InputType.TYPE_NULL;
    }
}