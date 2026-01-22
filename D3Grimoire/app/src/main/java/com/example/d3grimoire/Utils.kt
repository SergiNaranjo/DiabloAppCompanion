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

        public fun trySetImageFromURL(url: String?, imageView: ImageView) {
            // Fetch images off the UI thread and post results back to the main thread.
            val executor: ExecutorService = Executors.newSingleThreadExecutor();
            val handler: Handler = Handler(Looper.getMainLooper());
            var image: Bitmap?;

            executor.execute {
                try {
                    val `in` = URL(url).openStream();
                    image = BitmapFactory.decodeStream(`in`);
                    handler.post {
                        imageView.setImageBitmap(image)
                    }
                }
                catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }
    }
}
