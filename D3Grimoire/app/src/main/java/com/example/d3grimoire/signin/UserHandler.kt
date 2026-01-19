package com.example.d3grimoire.signin

import android.content.Context
import android.content.SharedPreferences
import com.example.d3grimoire.R
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import kotlin.text.iterator

class UserHandler {
    companion object {

        public lateinit var playerPrefs: SharedPreferences;

        public fun Init(context: Context) {
            playerPrefs = context.getSharedPreferences(
                context.getString(R.string.user_shared_preferences_key),
                Context.MODE_PRIVATE
            );
        }

        public fun isSignedIn(context: Context): Boolean {
            return getUserId(context) != null;
        }

        public fun getUserNative(context: Context): String? {
            val lastUser: String? = playerPrefs.getString("user", "");
            if (lastUser != "") return lastUser;
            return null;
        }

        public fun getPassNative(context: Context): Int? {
            val pass: Int? = playerPrefs.getInt("password", 0);
            if (pass != 0) return pass;
            return null;
        }

        public fun setUserNative(context: Context, user: String?, password: Int) {
            playerPrefs.edit().putString("user", user).apply();
            playerPrefs.edit().putInt("password", password).apply();
        }

        public fun getUserGoogle(context: Context): GoogleSignInAccount? {
            return GoogleSignIn.getLastSignedInAccount(context);
        }

        public fun signOutGoogle(context: Context) {
            val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken("554317598986-ltodp92d23e69tsbcedcbqofqpee0s30.apps.googleusercontent.com")
                .requestEmail()
                .build();

            val googleSignInClient = GoogleSignIn.getClient(context, gso);
            googleSignInClient.signOut();
        }

        public fun signOutNative(context: Context) {
            //Sets user to empty string, which is considered signed out
            playerPrefs.edit().putString("user", "").apply();
        }

        public fun signOut(context: Context) {
            signOutGoogle(context);
            signOutNative(context);
        }

        //Basic password hashing for encryption
        public fun encryptPass(password: String): Int {
            //Constants for encryption
            val p = 31;
            val m = 1000000009;

            var p_pow = 1;
            var sum = 0;
            for (letter in password) {
                sum = (sum + letter.code * p_pow).mod(m);
                p_pow = (p_pow * p).mod(m);
            }
            return sum;
        }

        public fun getUserId(context: Context): String? {
            val googleAccount: GoogleSignInAccount? = getUserGoogle(context);
            googleAccount?.id?.let { return it; }
            googleAccount?.email?.let { return it; }
            googleAccount?.displayName?.let { return it; }
            return getUserNative(context);
        }

        public fun getUserDisplayName(context: Context): String? {
            val googleAccount: GoogleSignInAccount? = getUserGoogle(context);
            googleAccount?.displayName?.let { return it; }
            return getUserNative(context);
        }

        public fun getUsername(context: Context): String? {
            return getUserDisplayName(context);
        }

    }
}
