package com.example.d3grimoire.signin

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.d3grimoire.FirebaseHandler
import com.example.d3grimoire.NavBarActivity
import com.example.d3grimoire.R
import com.example.d3grimoire.profile.ProfileActivity
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.SignInButton
import com.google.android.gms.tasks.Task
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.Query
import  com.example.d3grimoire.ActivityCaster

class SignInActivity : Fragment(R.layout.activity_sign_in) {

    private lateinit var googleSignInClient: GoogleSignInClient;

    companion object {
        private const val GOOGLE_SIGN_IN_REQUEST_CODE: Int = 9001
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        //User-Password sign in
        val loginButton: TextView = view.findViewById<TextView>(R.id.login_btn);
        loginButton.setOnClickListener { signIn(view); }

        //Google Sign in
        val gso: GoogleSignInOptions = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(UserHandler.GOOGLE_WEB_CLIENT_ID)
            .requestEmail()
            .build();

        googleSignInClient = GoogleSignIn.getClient(requireActivity(), gso);

        view.findViewById<SignInButton>(R.id.btn_sign_in).setOnClickListener {
            googleSignIn();
        }

        //Sign up
        val signUpButton: TextView = view.findViewById<TextView>(R.id.sign_up_btn);
        signUpButton.setOnClickListener {
            val act: NavBarActivity = ActivityCaster.getNavBarFromFragment(this);
            act.setFloatingButtonsVisibility(View.GONE);
            act.loadFragment(SignUpActivity());
        }
    }

    private fun signIn(view: View) {
        if (UserHandler.isSignedIn(this.requireActivity())) {
            Log.e("Login", "User already signed in!");
        }

        val user: String? = view.findViewById<EditText>(R.id.username).text.toString();
        val pass: String? = view.findViewById<EditText>(R.id.password).text.toString();

        val query: Query = FirebaseHandler.usersReference.orderByChild("user").equalTo(user);
        query.get()
            .addOnSuccessListener { snapshot ->
                checkPassword(snapshot, user, pass);
            }
            .addOnFailureListener { exception ->
                val message: String? = exception.message;
                message?.let { Log.e("Sign In", message) }
            }
    }

    private fun checkPassword(snapshot: DataSnapshot, user: String?, pass: String?) {
        if (snapshot.exists()) {
            for (dataSnapshot in snapshot.children) {
                val hashedPass: Int? = dataSnapshot.child("password").getValue(Int::class.java);
                pass?.let {
                    if (hashedPass == UserHandler.encryptPass(pass)) {
                        UserHandler.setUserNative(user, hashedPass);
                        ActivityCaster.getNavBarFromFragment(this).loadFragment(ProfileActivity());
                    }
                } ?: run {
                    Log.e("Login", "User has no passwprd")
                }
            }
        } else {
            Log.e("Login", "User not registered")
        }
    }

    private fun googleSignIn() {
        val signInIntent: Intent = googleSignInClient.signInIntent;
        startActivityForResult(signInIntent, GOOGLE_SIGN_IN_REQUEST_CODE);
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode != GOOGLE_SIGN_IN_REQUEST_CODE) return;

        val task: Task<GoogleSignInAccount> = GoogleSignIn.getSignedInAccountFromIntent(data);
        if (task.isSuccessful)
            ActivityCaster.getNavBarFromFragment(this).loadFragment(ProfileActivity());
        else
            Log.e("Login Google", "Error: ", task.exception);
    }
}
