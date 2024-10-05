package com.example.kipps_bot

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.graphics.Bitmap
import android.net.ConnectivityManager
import android.net.Network
import android.net.http.SslError
import android.util.Log
import android.webkit.SslErrorHandler
import android.webkit.WebResourceError
import android.webkit.WebResourceRequest
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import java.net.URLEncoder

@Composable
fun ChatBot(
	modifier: Modifier = Modifier
) {
	val context = LocalContext.current
	
	// Custom HTML content with JavaScript code
	val htmlContent = """
    <html>
    <head>
        <title>Kipps Chatbot</title>
    </head>
    <body>
        <iframe
            src="https://app.kipps.ai/iframe/5e41b42f-52d3-4900-8446-378dcfdad048"
            title="Kipps.AI Chatbot"
            width="100%"
            style="height:100%; min-height:700px;"
            frameborder="0">
        </iframe>
    </body>
    </html>
"""
	
	val loadURL = remember {
		mutableStateOf("")
	}
	
	var isLoading by remember {
		mutableStateOf(false)
	}
	var networkReceiver: BroadcastReceiver? = null
	val connectivityManager =
		context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
	
	val networkCallback = remember {
		
		object : ConnectivityManager.NetworkCallback() {
			override fun onAvailable(network: Network) {
				super.onAvailable(network)
				loadURL.value = "data:text/html;charset=utf-8," + URLEncoder.encode(htmlContent, "UTF-8")
			}
			
			override fun onLost(network: Network) {
				super.onLost(network)
				loadURL.value = "file:///android_asset/offline.html"
			}
		}
	}
	
	LaunchedEffect(key1 = Unit) {
		
		val filter = IntentFilter().apply {
			addAction(ConnectivityManager.CONNECTIVITY_ACTION)
		}
		context.registerReceiver(networkReceiver, filter)
		connectivityManager.registerDefaultNetworkCallback(networkCallback)
		
		val networkState = connectivityManager.activeNetworkInfo
		if (networkState != null && networkState.isConnected) {
			loadURL.value = "data:text/html;charset=utf-8," + URLEncoder.encode(htmlContent, "UTF-8")
		} else {
			loadURL.value = "file:///android_asset/offline.html"
		}
		
	}
	
	DisposableEffect(key1 = Unit) {
		onDispose {
			context.unregisterReceiver(networkReceiver)
			connectivityManager.unregisterNetworkCallback(networkCallback)
		}
	}
	
	networkReceiver = object : BroadcastReceiver() {
		override fun onReceive(context: Context?, intent: Intent?) {
			if (intent?.action == ConnectivityManager.CONNECTIVITY_ACTION) {
				val connectivityManager =
					context?.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
				val networkInfo = connectivityManager.activeNetworkInfo
				if (networkInfo != null && networkInfo.isConnected) {
					loadURL.value = "data:text/html;charset=utf-8," + URLEncoder.encode(htmlContent, "UTF-8")
				} else {
					loadURL.value = "file:///android_asset/offline.html"
				}
			}
		}
	}
	
	AndroidView(
		modifier = modifier
			.wrapContentHeight(),
		factory = { ctx ->
			WebView(ctx).apply {
				settings.apply {
					javaScriptEnabled = true // Enable JavaScript
					domStorageEnabled = true // Enable DOM storage
					loadWithOverviewMode = true // Adjust content to fit screen width
					useWideViewPort = true
					mixedContentMode = WebSettings.MIXED_CONTENT_ALWAYS_ALLOW // Allow mixed content
					allowFileAccess = true
					allowContentAccess = true
				}
				
				webViewClient = object : WebViewClient() {
					override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
						super.onPageStarted(view, url, favicon)
						isLoading = true
						Log.d("WebView", "Page started loading: $url")
					}
					
					override fun onPageFinished(view: WebView?, url: String?) {
						super.onPageFinished(view, url)
						isLoading = false
						Log.d("WebView", "Page finished loading: $url")
					}
					
					override fun onReceivedError(
						view: WebView?,
						request: WebResourceRequest?,
						error: WebResourceError?
					) {
						super.onReceivedError(view, request, error)
						isLoading = false
						Log.e("WebView", "Error received: ${error?.description}")
					}
					
					override fun onReceivedSslError(
						view: WebView?,
						handler: SslErrorHandler?,
						error: SslError?
					) {
						handler?.proceed()  // Ignore SSL errors for now
						Log.w("WebView", "SSL Error received: ${error?.primaryError}")
					}
				}
			}
		},
		update = {
			// Load the custom HTML content with embedded JavaScript
			it.loadDataWithBaseURL(null, htmlContent, "text/html", "UTF-8", null)
//			it.loadUrl(loadURL.value)
		}
	)
	
	// Loading indicator when the page is still loading
	if (isLoading) {
		CircularProgressIndicator(
			modifier = Modifier
				.fillMaxWidth()
				.wrapContentHeight()
				.wrapContentSize(Alignment.Center)
		)
	}
}