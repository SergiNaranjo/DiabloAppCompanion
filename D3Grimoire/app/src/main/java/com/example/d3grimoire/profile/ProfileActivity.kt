package com.example.d3grimoire.profile

import android.content.Context
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
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.add
import androidx.fragment.app.commit
import com.example.d3grimoire.FirebaseHandler
import com.example.d3grimoire.R
import com.example.d3grimoire.Utils
import com.example.d3grimoire.signin.SignInActivity
import com.example.d3grimoire.signin.UserHandler
import com.google.firebase.database.Query
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.io.InputStream
import java.net.URL
import java.util.concurrent.Executor
import java.util.concurrent.Executors

class ProfileActivity : Fragment(R.layout.activity_profile) {

    private lateinit var imageUrlEditText: EditText;
    private lateinit var statusEditText: EditText;
    private var editState: Boolean = false;

    companion object {
        private const val PROFILE_PICTURE_REFRESH_DELAY_MS: Long = 500L
        private const val USER_FIELD_USER: String = "user"
        private const val USER_FIELD_PASSWORD: String = "password"
        private const val USER_FIELD_IMAGE_URL: String = "imgUrl"
        private const val USER_FIELD_STATUS: String = "status"
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (!UserHandler.isSignedIn(requireActivity()))
            Utils.getNavBarFromFragment(this).loadFragment(SignInActivity());

        val signOutButton: TextView = view.findViewById<TextView>(R.id.btn_sign_out);
        signOutButton.setOnClickListener { signOut(); };

        imageUrlEditText = view.findViewById<EditText>(R.id.profile_img_url);
        statusEditText = view.findViewById<EditText>(R.id.profile_status);

        setEditState(editState);

        loadUsername(view);
        loadStatus(view);
        loadProfilePicture(view);

        val editButton: ImageButton = view.findViewById<ImageButton>(R.id.profile_edit_btn);
        editButton.setOnClickListener { editProfile(view); };

        makeHeroData();

        for (i in heroIds.indices) {
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
        val query: Query = FirebaseHandler.usersReference.orderByChild(USER_FIELD_USER)
            .equalTo(UserHandler.getUserId(requireActivity()));
        query.get()
            .addOnSuccessListener { snapshot ->
                if (!isAdded) return@addOnSuccessListener
                if (!snapshot.exists()) return@addOnSuccessListener;

                Utils.trySetImageFromURL(
                    snapshot.children.first().child(USER_FIELD_IMAGE_URL).getValue(String::class.java),
                    view.findViewById<ImageView>(R.id.profile_picture)
                );
            }
            .addOnFailureListener { exception ->
                val message: String? = exception.message;
                message?.let { Log.e("Profile", message) }
            }
    }

    private fun editProfile(view: View) {
        if (!editState) {
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
                    delay(PROFILE_PICTURE_REFRESH_DELAY_MS);

                    Utils.getNavBarFromFragmentActivity(requireActivity())
                        .loadFragment(ProfileActivity());
                }
            } catch (e: Exception) {
                e.printStackTrace();
            }
        }
    }

    private fun pushEdit(view: View) {
        val query: Query = FirebaseHandler.usersReference.orderByChild(USER_FIELD_USER).equalTo(
            UserHandler.getUserId(requireActivity())
        );
        query.get()
            .addOnSuccessListener { snapshot ->
                if (!snapshot.exists()) return@addOnSuccessListener;

                val imgUrl: String = imageUrlEditText.text.toString();
                val status: String = statusEditText.text.toString();

                for (dataSnapshot in snapshot.children) {
                    val key: String? = dataSnapshot.key;
                    key?.let {
                        Log.d("Profile", key);
                        FirebaseHandler.usersReference.child(key).setValue(
                            mapOf(
                                USER_FIELD_USER to UserHandler.getUserId(requireActivity()),
                                USER_FIELD_PASSWORD to UserHandler.getPassNative(requireActivity()),
                                USER_FIELD_IMAGE_URL to imgUrl,
                                USER_FIELD_STATUS to status
                            )
                        );
                    }
                }
            }
            .addOnFailureListener { exception ->
                val message: String? = exception.message;
                message?.let { Log.e("Profile", message) }
            }
    }

    private fun signOut() {
        UserHandler.signOut(requireActivity());
        Utils.getNavBarFromFragment(this).loadFragment(ProfileActivity());
    }

    private fun loadUsername(view: View) {
        val usernameText: TextView = view.findViewById<TextView>(R.id.profile_username);
        val username: String? = UserHandler.getUserDisplayName(requireActivity());
        username?.let { usernameText.text = username; } ?: run {
            usernameText.text =
                getString(R.string.profile_username_default);
        }
    }

    private fun loadStatus(view: View) {
        var status: String? = null;

        val query: Query = FirebaseHandler.usersReference.orderByChild(USER_FIELD_USER)
            .equalTo(UserHandler.getUserId(requireActivity()));
        query.get()
            .addOnSuccessListener { snapshot ->
                if (!isAdded) return@addOnSuccessListener
                val ctx: Context = context ?: return@addOnSuccessListener
                if (snapshot.exists()) {
                    for (dataSnapshot in snapshot.children) {
                        status = dataSnapshot.child(USER_FIELD_STATUS).getValue(String::class.java);
                    }
                }
                if (!isAdded) return@addOnSuccessListener
                status?.let {
                    statusEditText.text = Editable.Factory.getInstance().newEditable(status);
                } ?: run {
                    statusEditText.text = Editable.Factory.getInstance().newEditable(
                        ctx.getString(R.string.profile_status_default)
                    );
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
