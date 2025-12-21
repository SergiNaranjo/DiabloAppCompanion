package com.example.d3grimoire

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.ImageButton
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.add
import androidx.fragment.app.commit
import com.example.d3grimoire.posts.community.CommunityPost
import com.example.d3grimoire.posts.community.communityNewsButtonData
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.SignInButton
import com.google.android.gms.common.api.ApiException
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.Query

class SignInActivity : Fragment(R.layout.sign_in_screen) {

    private lateinit var googleSignInClient: GoogleSignInClient;
    private lateinit var database: DatabaseReference

    val p: Int = 31;
    val m: Int = 1000000009;

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        //User-Password sign in
        val loginButton: ImageButton = view.findViewById<ImageButton>(R.id.login_btn);
        loginButton.setOnClickListener { signIn(view); }

        //Google Sign in
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken("554317598986-ltodp92d23e69tsbcedcbqofqpee0s30.apps.googleusercontent.com")
            .requestEmail()
            .build();

        googleSignInClient = GoogleSignIn.getClient(requireActivity(), gso);

        val account = GoogleSignIn.getLastSignedInAccount(requireActivity());
        account?.let {
            Log.d("Login Google", "You already have: " + account.displayName);
        }
            ?: run {
                Log.d("Login Google", "No account yet");
                view.findViewById<SignInButton>(R.id.btn_sign_in).setOnClickListener {
                    googleSignIn();
                }
            }
    }

    private fun signIn(view: View) {

        var playerPrefs: SharedPreferences =
            requireActivity().getSharedPreferences("prefs_user", Context.MODE_PRIVATE)
        val lastUser = playerPrefs.getString("user", "");
        if(lastUser != "") {
            Log.d("Login", "User is already: " + lastUser)
        } else {
            Log.d("Login", "No user yet");
        }

        val user: String? = view.findViewById<EditText>(R.id.username).text.toString();
        val pass: String? = view.findViewById<EditText>(R.id.password).text.toString();

        val databaseUrl =
            "https://appcompanion-eedc3-default-rtdb.europe-west1.firebasedatabase.app/"
        database = FirebaseDatabase.getInstance(databaseUrl)
            .getReference("users")

        val query: Query = database.orderByChild("user").equalTo(user);
        query.get()
            .addOnSuccessListener { snapshot ->
                if(snapshot.exists()) {
                    for (dataSnapshot in snapshot.children) {
                        val hashedPass: Int? = dataSnapshot.child("password").getValue(Int::class.java);
                        pass?.let {
                            if(hashedPass == encryptPass(pass)) {
                                //Correct login!!!!
                                Log.d("Login", "Success! User is: " + user);
                                var playerPrefs: SharedPreferences =
                                    requireActivity().getSharedPreferences("prefs_user", Context.MODE_PRIVATE)
                                playerPrefs.edit().putString("user", user).apply();
                            }
                        } ?: run {
                            Log.d("Login", "User has no passwprd")
                        }
                    }
                } else {
                    Log.d("Login", "User not registered")
                }
            }
            .addOnFailureListener { exception ->
                println("Error: ${exception.message}")
            }
    }

    private fun googleSignIn() {
        val signInIntent: Intent = googleSignInClient.signInIntent;
        startActivityForResult(signInIntent, 9001);
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if(requestCode == 9001) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data);
            if(task.isSuccessful) {
                val account = task.getResult(ApiException::class.java);
                Log.d("Login Google", "Got account for: " + account.displayName)
            } else {
                Log.e("Login Google", "Error: ", task.exception);
            }
        }
    }

    private fun encryptPass(password: String): Int {
        var p_pow = 1;
        var sum = 0;
        for (letter in password) {
            sum = (sum + letter.code * p_pow).mod(m);
            p_pow = (p_pow * p).mod(m);
        }
        return sum;
    }
}