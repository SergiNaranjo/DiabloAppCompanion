package com.example.d3grimoire.signin

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.ImageButton
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import com.example.d3grimoire.NavBarActivity
import com.example.d3grimoire.R
import com.example.d3grimoire.posts.community.CommunityActivity
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.Query

class SignUpActivity : Fragment(R.layout.activity_sign_up) {

    private lateinit var googleSignInClient: GoogleSignInClient;
    private lateinit var database: DatabaseReference

    companion object {
        private const val POSTS_REFERENCE: String = "posts"
        private const val USERS_REFERENCE: String = "users"
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState);

        val signUpConfirmButton: ImageButton = view.findViewById<ImageButton>(R.id.sign_up_confirm);
        val exitButton: ImageButton = view.findViewById<ImageButton>(R.id.sign_up_exit);

        val databaseUrl: String = getString(R.string.database_URL);
        database = FirebaseDatabase.getInstance(databaseUrl)
            .getReference(POSTS_REFERENCE);

        signUpConfirmButton.setOnClickListener {
            trySignUp(view);
        }

        exitButton.setOnClickListener {
            exitToSignIn();
        }
    }

    private fun exitToSignIn() {
        val activity: FragmentActivity = requireActivity();
        if (activity !is NavBarActivity) throw Exception("Invalid root node!");
        activity.loadFragment(SignInActivity());
    }

    private fun trySignUp(view: View) {
        val activity: FragmentActivity = requireActivity();
        if(activity is AppCompatActivity &&
            UserHandler.getUserNative(activity) != null) {
            Log.d("Login", "User already signed in!");
        }

        val user: String = view.findViewById<EditText>(R.id.sign_up_user).text.toString();
        val pass: String = view.findViewById<EditText>(R.id.sign_up_password).text.toString();
        val passConfirm: String = view.findViewById<EditText>(R.id.sign_up_confirm_password).text.toString();

        if(user == "" || pass == "" || passConfirm == "") return;

        if(pass != passConfirm) {
            showPasswordsDifferentAlert();
            return;
        }

        database = FirebaseDatabase.getInstance(getString(R.string.database_URL))
            .getReference(USERS_REFERENCE)

        val query: Query = database.orderByChild("user").equalTo(user);
        query.get()
            .addOnSuccessListener { snapshot ->
                if(snapshot.exists()) {
                    Log.d("Sign Up", "User already exists!");
                    showUserExistsAlert();
                    return@addOnSuccessListener;
                }

                //Valid user - Add to db
                val dataId: String? = database.push().key;
                dataId?.let {
                    database.child(dataId)
                        .setValue(mapOf(
                            "user" to user,
                            "password" to UserHandler.encryptPass(pass)
                        ))
                        .addOnFailureListener {
                            Log.e("Firebase", "Write failed", it)
                        };
                }

                val act: FragmentActivity = requireActivity();
                if (act !is NavBarActivity) throw Exception("Invalid root node!");
                else {
                    act.setFloatingButtonsVisibility(View.GONE);
                    act.loadFragment(SignInActivity());
                }
            }
            .addOnFailureListener { exception ->
                Log.e("Firebase", "Exception: ${exception.message}");
            }
    }

    private fun showUserExistsAlert() {
        val builder: AlertDialog.Builder = AlertDialog.Builder(requireActivity());

        builder.setMessage("The user already exists!");
        builder.setTitle("");
        builder.setCancelable(false);
        builder.setPositiveButton("Continue") { dialog, which ->
            dialog.cancel();
        }

        val alertDialog: AlertDialog = builder.create();
        alertDialog.show();
    }

    private fun showPasswordsDifferentAlert() {
        val builder: AlertDialog.Builder = AlertDialog.Builder(requireActivity());

        builder.setMessage("The passwords are different!");
        builder.setTitle("");
        builder.setCancelable(false);
        builder.setPositiveButton("Continue") { dialog, which ->
            dialog.cancel();
        }

        val alertDialog: AlertDialog = builder.create();
        alertDialog.show();
    }
}
