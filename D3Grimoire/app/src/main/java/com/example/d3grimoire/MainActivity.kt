package com.example.d3grimoire

import android.os.Bundle
import android.view.animation.LinearInterpolator
import android.view.animation.RotateAnimation
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.splash_screen)

        val image = findViewById<ImageView>(R.id.imageView3)

        val rotate = RotateAnimation(
            0f,
            360f,
            RotateAnimation.RELATIVE_TO_SELF,
            0.5f,
            RotateAnimation.RELATIVE_TO_SELF,
            0.5f
        )

        rotate.duration = 2000
        rotate.repeatCount = RotateAnimation.INFINITE
        rotate.interpolator = LinearInterpolator()

        image.startAnimation(rotate)
    }
}