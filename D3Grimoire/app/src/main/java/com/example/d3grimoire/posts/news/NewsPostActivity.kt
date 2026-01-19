package com.example.d3grimoire.posts.news

import android.os.Bundle
import android.webkit.WebView
import android.webkit.WebSettings
import androidx.appcompat.app.AppCompatActivity
import com.example.d3grimoire.R

class NewsPostActivity : AppCompatActivity() {

    private var url : String? = null;
    private lateinit var webView : WebView;

    companion object {
        const val EXTRA_URL: String = "url"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_news_post)

        webView = findViewById<WebView>(R.id.news_post_webview);

        val webSettings: WebSettings = webView.settings
        webSettings.javaScriptEnabled = true
        webSettings.setSupportZoom(true)
        webSettings.builtInZoomControls = true
        webSettings.displayZoomControls = false
        webSettings.loadsImagesAutomatically = true
        webSettings.useWideViewPort = true
        webSettings.loadWithOverviewMode = true

        url = intent.extras?.getString(EXTRA_URL);
        assert(url != null);
        url?.let { webView.loadUrl(url!!); };

    }
}
