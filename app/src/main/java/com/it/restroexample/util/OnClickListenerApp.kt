package com.it.restroexample.util

import android.view.View

class OnClickListenerApp(private var position: Int, var url:String, onClickCallback: OnClickCallback) : View.OnClickListener {
    private var onClickCallback: OnClickCallback? = onClickCallback
    override fun onClick(view: View) {
        onClickCallback?.onClicked(view, position, url)
    }
    interface OnClickCallback {
        fun onClicked(view: View, position: Int, url:String)
    }
}
