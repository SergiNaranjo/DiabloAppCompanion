package com.example.d3grimoire

import android.os.Bundle
import android.webkit.WebView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class NewsPost : AppCompatActivity() {

    private lateinit var url : String;
    private lateinit var webView : WebView;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_news_post)

        webView = findViewById<WebView>(R.id.news_post_webview);
        url = intent.extras!!.getString("url")!!;
        webView.loadUrl(url);

    }
}