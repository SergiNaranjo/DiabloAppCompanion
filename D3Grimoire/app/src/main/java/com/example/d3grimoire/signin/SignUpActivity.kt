package com.example.d3grimoire.signin

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.ImageButton
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import com.example.d3grimoire.FirebaseHandler
import com.example.d3grimoire.NavBarActivity
import com.example.d3grimoire.R
import com.google.firebase.database.Query
import com.example.d3grimoire.ActivityCaster

class SignUpActivity : Fragment(R.layout.activity_sign_up) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState);

        val signUpConfirmButton: ImageButton = view.findViewById<ImageButton>(R.id.sign_up_confirm);
        val exitButton: ImageButton = view.findViewById<ImageButton>(R.id.sign_up_exit);

        signUpConfirmButton.setOnClickListener {
            trySignUp(view);
        }

        exitButton.setOnClickListener {
            exitToSignIn();
        }
    }

    private fun exitToSignIn() {
        ActivityCaster.getNavBarFromFragment(this).loadFragment(SignInActivity());
    }

    private fun trySignUp(view: View) {
        if(UserHandler.isSignedIn(ActivityCaster.getAppCompatFromFragment(this)))
            Log.e("Login", "User already signed in!");

        val user: String = view.findViewById<EditText>(R.id.sign_up_user).text.toString();
        val pass: String = view.findViewById<EditText>(R.id.sign_up_password).text.toString();
        val passConfirm: String = view.findViewById<EditText>(R.id.sign_up_confirm_password).text.toString();

        if(user == "" || pass == "" || passConfirm == "") return;

        if(pass != passConfirm) {
            showPasswordsDifferentAlert();
            return;
        }

        val query: Query = FirebaseHandler.usersReference.orderByChild("user").equalTo(user);
        query.get()
            .addOnSuccessListener { snapshot ->
                if(snapshot.exists()) {
                    Log.e("Sign Up", "User already exists!");
                    showUserExistsAlert();
                    return@addOnSuccessListener;
                }

                //Valid user - Add to db
                val dataId: String? = FirebaseHandler.usersReference.push().key;
                dataId?.let {
                    FirebaseHandler.usersReference.child(dataId)
                        .setValue(mapOf(
                            "user" to user,
                            "password" to UserHandler.encryptPass(pass)
                        ))
                        .addOnFailureListener {
                            Log.e("Firebase", "Write failed", it)
                        };
                }

                val act: NavBarActivity = ActivityCaster.getNavBarFromFragment(this);
                act.setFloatingButtonsVisibility(View.GONE);
                act.loadFragment(SignInActivity());
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
