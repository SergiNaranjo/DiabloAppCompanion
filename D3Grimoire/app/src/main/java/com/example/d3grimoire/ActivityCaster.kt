package com.example.d3grimoire

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Handler
import android.os.Looper
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import java.net.URL
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class ActivityCaster {
    companion object {
        public fun getAppCompatFromFragment(fragment: Fragment) : AppCompatActivity {
            val activity: FragmentActivity = fragment.requireActivity();
            if(activity !is AppCompatActivity) throw Exception("Invalid root activity!");
            return activity;
        }

        public fun getNavBarFromFragment(fragment: Fragment) : NavBarActivity {
            val activity: FragmentActivity = fragment.requireActivity();
            if(activity !is NavBarActivity) throw Exception("Invalid root activity!");
            return activity;
        }

        public fun getNavBarFromFragmentActivity(activity: FragmentActivity) : NavBarActivity {
            if(activity !is NavBarActivity) throw Exception("Invalid root activity!");
            return activity;
        }
    }
}
