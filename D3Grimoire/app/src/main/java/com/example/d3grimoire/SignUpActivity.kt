package com.example.d3grimoire

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.ImageButton
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.Query

class SignUpActivity : Fragment(R.layout.activity_sign_up) {

    private lateinit var googleSignInClient: GoogleSignInClient;
    private lateinit var database: DatabaseReference

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState);

        val signUpConfirmButton: ImageButton = view.findViewById<ImageButton>(R.id.sign_up_confirm);
        val exitButton: ImageButton = view.findViewById<ImageButton>(R.id.sign_up_exit);

        val databaseUrl =
            "https://appcompanion-eedc3-default-rtdb.europe-west1.firebasedatabase.app/";
        database = FirebaseDatabase.getInstance(databaseUrl)
            .getReference("posts");

        signUpConfirmButton.setOnClickListener {
            trySignUp(view);
        }

        exitButton.setOnClickListener {
            exitToSignIn();
        }
    }

    private fun exitToSignIn() {
        TODO("Not yet implemented")
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
            .getReference("users")

        val query: Query = database.orderByChild("user").equalTo(user);
        query.get()
            .addOnSuccessListener { snapshot ->
                if(snapshot.exists()) {
                    Log.d("Sign Up", "User already exists!");
                    showUserExistsAlert();
                    return@addOnSuccessListener;
                }

                //Valid user - Add to db
                val dataId = database.push().key;
                dataId?.let {
                    database.child(dataId)
                        .setValue(mapOf(
                            "user" to user,
                            "password" to UserHandler.encryptPass(pass)
                        ))
                        .addOnFailureListener {
                            Log.e("FIREBASE", "Write failed", it)
                        };
                }

                val act = requireActivity();
                if (act !is NavBarActivity) throw Exception("Invalid root node!");
                else {
                    act.setFloatingButtonsVisibility(View.GONE);
                    act.loadFragment(SignInActivity());
                }
            }
            .addOnFailureListener { exception ->
                Log.e("FIREBASE", "Exception: ${exception.message}");
            }
    }

    private fun showUserExistsAlert() {
        val builder = AlertDialog.Builder(requireActivity());

        builder.setMessage("The user already exists!");
        builder.setTitle("");
        builder.setCancelable(false);
        builder.setPositiveButton("Continue") { dialog, which ->
            dialog.cancel();
        }

        val alertDialog = builder.create();
        alertDialog.show();
    }

    private fun showPasswordsDifferentAlert() {
        val builder = AlertDialog.Builder(requireActivity());

        builder.setMessage("The passwords are different!");
        builder.setTitle("");
        builder.setCancelable(false);
        builder.setPositiveButton("Continue") { dialog, which ->
            dialog.cancel();
        }

        val alertDialog = builder.create();
        alertDialog.show();
    }
}