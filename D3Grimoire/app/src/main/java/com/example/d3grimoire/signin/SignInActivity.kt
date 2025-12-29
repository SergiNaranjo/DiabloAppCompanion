package com.example.d3grimoire.signin

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import com.example.d3grimoire.NavBarActivity
import com.example.d3grimoire.R
import com.example.d3grimoire.profile.ProfileActivity
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.SignInButton
import com.google.android.gms.common.api.ApiException
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.Query

class SignInActivity : Fragment(R.layout.activity_sign_in) {

    private lateinit var googleSignInClient: GoogleSignInClient;
    private lateinit var database: DatabaseReference

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        //User-Password sign in
        val loginButton: TextView = view.findViewById<TextView>(R.id.login_btn);
        loginButton.setOnClickListener { signIn(view); }

        //Google Sign in
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken("554317598986-ltodp92d23e69tsbcedcbqofqpee0s30.apps.googleusercontent.com")
            .requestEmail()
            .build();

        googleSignInClient = GoogleSignIn.getClient(requireActivity(), gso);

        val account = UserHandler.getUserGoogle(requireActivity());
        if (account == null)
            view.findViewById<SignInButton>(R.id.btn_sign_in).setOnClickListener {
                googleSignIn();
            }

        //Sign up
        val signUpButton: TextView = view.findViewById<TextView>(R.id.sign_up_btn);
        signUpButton.setOnClickListener {
            val act = requireActivity();
            if (act !is NavBarActivity) throw Exception("Invalid root node!");
            else {
                act.setFloatingButtonsVisibility(View.GONE);
                act.loadFragment(SignUpActivity());
            }
        }
    }

    private fun signIn(view: View) {
        val activity: FragmentActivity = requireActivity();
        if (activity is AppCompatActivity &&
            UserHandler.getUserNative(activity) != null
        ) {
            Log.d("Login", "User already signed in!");
        }

        val user: String? = view.findViewById<EditText>(R.id.username).text.toString();
        val pass: String? = view.findViewById<EditText>(R.id.password).text.toString();

        database = FirebaseDatabase.getInstance(getString(R.string.database_URL))
            .getReference("users")

        val query: Query = database.orderByChild("user").equalTo(user);
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
                        val activity: FragmentActivity = requireActivity();
                        if (activity !is NavBarActivity) throw Exception("Invalid root node!");
                        UserHandler.setUserNative(activity, user, hashedPass);
                        activity.loadFragment(ProfileActivity());
                    }
                } ?: run {
                    Log.d("Login", "User has no passwprd")
                }
            }
        } else {
            Log.d("Login", "User not registered")
        }
    }

    private fun googleSignIn() {
        val signInIntent: Intent = googleSignInClient.signInIntent;
        startActivityForResult(signInIntent, 9001);
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == 9001) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data);
            if (task.isSuccessful) {
                val account = task.getResult(ApiException::class.java);
                val activity: FragmentActivity = requireActivity();
                if (activity !is NavBarActivity) throw Exception("Invalid root node!");
                activity.loadFragment(ProfileActivity());
            } else {
                Log.e("Login Google", "Error: ", task.exception);
            }
        }
    }
}