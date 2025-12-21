package com.example.d3grimoire

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.common.SignInButton
import com.google.android.gms.common.api.ApiException

class UserHandler {

    public enum class UserMode {
        GOOGLE,
        NATIVE
    }

    companion object {

        public fun getUserNative(activity: AppCompatActivity): String? {
            var playerPrefs: SharedPreferences =
                activity.getSharedPreferences("prefs_user", Context.MODE_PRIVATE)
            val lastUser: String? = playerPrefs.getString("user", "");
            if (lastUser != "") return lastUser;
            return null;
        }

        public fun setUserNative(activity: AppCompatActivity, user: String?) {
            val playerPrefs: SharedPreferences =
                activity.getSharedPreferences("prefs_user", Context.MODE_PRIVATE)
            playerPrefs.edit().putString("user", user).apply();
        }

        public fun getUserGoogle(context: Context): GoogleSignInAccount? {
            return GoogleSignIn.getLastSignedInAccount(context);
        }

    }
}