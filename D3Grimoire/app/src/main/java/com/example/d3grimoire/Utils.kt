package com.example.d3grimoire

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity

class Utils {
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