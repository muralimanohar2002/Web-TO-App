@file:Suppress("DEPRECATION")


package com.example.webview

import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkInfo
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.view.KeyEvent
import android.webkit.WebResourceError
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.appcompat.app.AlertDialog
import com.example.webview.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private val URL = "https://github.com/"
    private var isAlreadyCreated = false
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()

        binding.webViews.settings.javaScriptEnabled = true
        binding.webViews.settings.setSupportZoom(false)

        binding.webViews.webViewClient = object : WebViewClient(){
            override fun onReceivedError(view: WebView?, request: WebResourceRequest?, error: WebResourceError?) {
                super.onReceivedError(view, request, error)
                showError("Error", "No internet connection. Please check your connection.", this@MainActivity)
            }
        }
        binding.webViews.loadUrl(URL)
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        if(keyCode == KeyEvent.KEYCODE_BACK && binding.webViews.canGoBack()){
            binding.webViews.goBack();
            return true
        }
        return super.onKeyDown(keyCode, event)
    }

    override fun onResume() {
        super.onResume()
        if(isAlreadyCreated && !isNetworkAvailable()){
            isAlreadyCreated=false
            showError("Error", "No internet connection. Please check your connection.", this@MainActivity)
        }
    }

    private fun isNetworkAvailable(): Boolean {
        val connection = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetwork: NetworkInfo? = connection.activeNetworkInfo
        val isConnected: Boolean = activeNetwork?.isConnectedOrConnecting == true

        return isConnected
    }


    private fun showError(title: String, message: String, context: Context) {
        val dialog = AlertDialog.Builder(context)
        dialog.setTitle(title)
        dialog.setMessage(message)
        dialog.setNegativeButton("Cancel") { _, _ ->
            this.finish()
        }
        dialog.setNeutralButton("Setting") {_, _ ->
            startActivity(Intent(Settings.ACTION_SETTINGS))
        }
        dialog.setPositiveButton("Retry"){_, _ ->
            this.recreate()
        }
        dialog.create().show()
    }

}