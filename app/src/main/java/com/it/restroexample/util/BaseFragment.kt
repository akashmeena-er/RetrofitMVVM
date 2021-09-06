package com.it.restroexample.util

import android.content.Context
import android.net.ConnectivityManager
import android.transition.Slide
import android.view.Gravity
import androidx.fragment.app.Fragment
import com.it.restroexample.R
import com.it.restroexample.dialog.DialogOK
import com.it.restroexample.dialog.DialogProgress

/**
 * created by Akash on 24/12/2020
 * This for reusable of method for fragments
 */

open class BaseFragment : Fragment() {
    private var dialogOK: DialogOK? = null
    fun dialogOK(context: Context, title: String, message: String) {
        if (dialogOK != null && dialogOK!!.isShowing)
            dialogOK!!.dismiss()
        dialogOK = DialogOK(context, title, message)
        dialogOK!!.show()
    }

    private var dialogProgress: DialogProgress? = null
    fun showProgressDialog() {
        if (activity != null) {
            if (dialogProgress != null && dialogProgress!!.isShowing)
                dialogProgress!!.dismiss()
            dialogProgress = DialogProgress(requireActivity())
            dialogProgress!!.show()
        }
    }

    fun hideProgressDialog() {
        if (dialogProgress != null && dialogProgress!!.isShowing)
            dialogProgress!!.dismiss()
    }
    val isNetworkAvailable: Boolean
        get() {
            try {
                val cm =
                    requireContext().getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
                val networkInfo = cm.activeNetworkInfo
                if (networkInfo != null && networkInfo.isConnected)
                    return true
            } catch (e: Exception) {
                e.printStackTrace()
            }
            return false
        }
    fun addFragmentWithBack(fragment: Fragment, fragmentTag: String) {
        try {
            if (activity == null) return
            fragment.enterTransition = Slide(Gravity.END)
            fragment.exitTransition = Slide(Gravity.START)
            requireActivity().supportFragmentManager.beginTransaction()
                .add(R.id.container, fragment, fragmentTag)
                .addToBackStack(null)
                .commitAllowingStateLoss()
        } catch (e: Exception) {
            e.printStackTrace()
        } catch (e: Error) {
            e.printStackTrace()
        }
    }
     fun replaceFragment(fragment: Fragment) {
        try {
            requireActivity().supportFragmentManager
                .beginTransaction()
                .replace(R.id.container, fragment, "Home")
                .commitAllowingStateLoss()
        } catch (e: Exception) {
            e.printStackTrace()
        } catch (e: Error) {
            e.printStackTrace()
        }
    }
}