package com.example.d3grimoire

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Handler
import android.os.Looper
import android.widget.ImageView
import java.net.URL
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class ImageDecoder {
    companion object {
        public fun trySetImageFromURL(url: String?, imageView: ImageView) {
            // Fetch images off the UI thread and post results back to the main thread.
            val executor: ExecutorService = Executors.newSingleThreadExecutor();
            val handler: Handler = Handler(Looper.getMainLooper());
            var image: Bitmap?;

            executor.execute {
                image = getImageFromUrl(url);
                handler.post {
                    imageView.setImageBitmap(image)
                }
            }
        }

        public fun isImageURLValid(url: String?): Boolean {
            //Ensure image url is valid by attempting to decode it
            try {
                val `in` = URL(url).openStream();
                val image: Bitmap? = BitmapFactory.decodeStream(`in`);
            }
            catch (e: Exception) {
                e.printStackTrace();
                return false;
            }

            return true;
        }

        public fun getImageFromUrl(url: String?): Bitmap? {

            if(!isImageURLValid(url)) return null;

            val `in` = URL(url).openStream();
            val image: Bitmap? = BitmapFactory.decodeStream(`in`);

            return image;
        }
    }
}