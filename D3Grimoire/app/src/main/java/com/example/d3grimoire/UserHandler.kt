package com.example.d3grimoire

import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInOptions

class UserHandler {
    companion object {

        public fun isSignedIn(activity: AppCompatActivity): Boolean {
            return getUsername(activity) != null;
        }

        public fun getUserNative(activity: AppCompatActivity): String? {
            var playerPrefs: SharedPreferences =
                activity.getSharedPreferences("prefs_user", Context.MODE_PRIVATE);
            val lastUser: String? = playerPrefs.getString("user", "");
            if (lastUser != "") return lastUser;
            return null;
        }

        public fun getPassNative(activity: AppCompatActivity): Int? {
            var playerPrefs: SharedPreferences =
                activity.getSharedPreferences("prefs_user", Context.MODE_PRIVATE);
            val pass: Int? = playerPrefs.getInt("password", 0);
            if (pass != 0) return pass;
            return null;
        }

        public fun setUserNative(activity: AppCompatActivity, user: String?, password: Int) {
            val playerPrefs: SharedPreferences =
                activity.getSharedPreferences("prefs_user", Context.MODE_PRIVATE);
            playerPrefs.edit().putString("user", user).apply();
            playerPrefs.edit().putInt("password", password).apply();
        }

        public fun getUserGoogle(context: Context): GoogleSignInAccount? {
            return GoogleSignIn.getLastSignedInAccount(context);
        }

        public fun signOutGoogle(activity: AppCompatActivity) {
            val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken("554317598986-ltodp92d23e69tsbcedcbqofqpee0s30.apps.googleusercontent.com")
                .requestEmail()
                .build();

            val googleSignInClient = GoogleSignIn.getClient(activity, gso);
            googleSignInClient.signOut();
        }

        public fun signOutNative(activity: AppCompatActivity) {
            //Sets user to empty string, which is considered signed out
            val playerPrefs: SharedPreferences =
                activity.getSharedPreferences("prefs_user", Context.MODE_PRIVATE);
            playerPrefs.edit().putString("user", "").apply();
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

        public fun getUsername(activity: AppCompatActivity): String? {
            var username: String? = getUserGoogle(activity)?.displayName;
            username?.let { return username; }
            username = getUserNative(activity);
            username?.let { return username }
            return null;
        }

    }
}