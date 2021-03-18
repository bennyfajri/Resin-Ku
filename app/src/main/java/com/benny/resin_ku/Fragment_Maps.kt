package com.benny.resin_ku

import android.graphics.Bitmap
import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import kotlinx.android.synthetic.main.fragment__maps.*

class Fragment_Maps : Fragment() {

    private val url = "https://yuanshen.site/"
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment__maps, container, false)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val settings = webView.settings;
        settings.javaScriptEnabled = true
        settings.setAppCacheEnabled(true)
        settings.cacheMode = WebSettings.LOAD_DEFAULT

        settings.setSupportZoom(true)
        settings.builtInZoomControls = true
        settings.displayZoomControls = false

        settings.blockNetworkImage = false
        settings.loadsImagesAutomatically = true

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN){
            settings.allowUniversalAccessFromFileURLs = true
        }

        webView.fitsSystemWindows = true

        webView.setLayerType(View.LAYER_TYPE_HARDWARE, null)
        webView.loadUrl(url)

        webView.webViewClient = object  : WebViewClient(){
            override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
                super.onPageStarted(view, url, favicon)
            }

            override fun onPageFinished(view: WebView?, url: String?) {
                super.onPageFinished(view, url)
            }
        }
    }



    companion object {
        fun newInstance(): Fragment{
            val fragment = Fragment_Maps()
            val args = Bundle()
            fragment.arguments = args
            return fragment
        }
    }
}