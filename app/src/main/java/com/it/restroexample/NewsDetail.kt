package com.it.restroexample

import android.os.Bundle
import android.view.InflateException
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebView
import android.webkit.WebViewClient
import com.it.restroexample.util.BaseFragment
import com.it.restroexample.util.BaseInterface.Companion.PN_FORM
import kotlinx.android.synthetic.main.news_details.*

/**
 * created by Akash on 24/12/2020
 */
class NewsDetail : BaseFragment(), View.OnClickListener {
    private var link = ""
    /**
     * get link from bunle for showing into webview in onCreateView
     */
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        var view: View? = null
        try {
            link = requireArguments().getString(PN_FORM)!!
            view = inflater.inflate(R.layout.news_details, container, false)
        } catch (e: InflateException) {
            e.printStackTrace()
        }
        return view
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (!isNetworkAvailable) {
            dialogOK(
                requireActivity(),
                resources.getString(R.string.whoops),
                resources.getString(R.string.network_error)
            )
        } else {
            loadUrlToWebView()

        }
        ivBack.setOnClickListener(this)

    }
    /**
     * load url that we got from last Home fragment into the webview
     */
    private fun loadUrlToWebView() {
        webview.scrollBarStyle = WebView.SCROLLBARS_OUTSIDE_OVERLAY
        /**
         *show loader when webview loading the URL
         */
        showProgressDialog()
        webview.webViewClient = object : WebViewClient() {
            override fun shouldOverrideUrlLoading(view: WebView, url: String): Boolean {
                view.loadUrl(url)
                return true
            }
            override fun onPageFinished(view: WebView, url: String) {
                hideProgressDialog()
            }
            override fun onReceivedError(
                view: WebView,
                errorCode: Int,
                description: String,
                failingUrl: String
            ) {

            }
        }
        webview.loadUrl(link)
    }
    override fun onClick(view: View) {
        when (view) {
            ivBack -> {
                requireActivity().onBackPressed()
            }
        }
    }
}
