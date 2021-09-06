package com.it.restroexample.dialog

import android.app.Dialog
import android.content.Context
import android.graphics.drawable.ColorDrawable
import android.text.TextUtils
import android.text.method.ScrollingMovementMethod
import android.view.View
import android.view.ViewGroup
import android.view.Window
import com.it.restroexample.R
import kotlinx.android.synthetic.main.dialog_ok.*
/**
 * created by Akash on 24/12/2020
 */
class DialogOK(context: Context, title: String, message: String) : Dialog(context) {
    var onClickCallback: OkClickCallback? = null

    init {
        try {
            requestWindowFeature(Window.FEATURE_NO_TITLE)
            setContentView(R.layout.dialog_ok)
            this.window!!.setBackgroundDrawable(ColorDrawable(0))
            this.window!!.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
            this.setCanceledOnTouchOutside(true)
            this.setCancelable(true)
            if (TextUtils.isEmpty(title))
                tvTitle!!.visibility = View.GONE
            else {
                tvTitle!!.visibility = View.VISIBLE
                tvTitle!!.text = title
            }
            tvMessage!!.text = message
            tvMessage!!.movementMethod = ScrollingMovementMethod()
            tvButton.setOnClickListener {
                if (onClickCallback != null) onClickCallback!!.onOk()
                dismiss()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    interface OkClickCallback {
        fun onOk()
    }
}
