package com.it.restroexample.dialog

import android.app.Dialog
import android.content.Context
import android.graphics.drawable.ColorDrawable
import android.view.Window
import com.it.restroexample.R
/**
 * created by Akash on 24/12/2020
 */
class DialogProgress(context: Context) : Dialog(context) {
    init {
        try {
            requestWindowFeature(Window.FEATURE_NO_TITLE)
            setContentView(R.layout.progress_loader)
            this.setCancelable(true)
            this.setCanceledOnTouchOutside(false)
            this.window!!.setBackgroundDrawable(ColorDrawable(0))
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}
