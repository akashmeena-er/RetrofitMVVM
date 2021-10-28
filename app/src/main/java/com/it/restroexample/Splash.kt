package com.it.restroexample
import android.os.Bundle
import android.os.Handler
import android.view.InflateException
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.it.restroexample.util.BaseFragment

/**
 * created by Akash on 24/12/2020
 */
class Splash : BaseFragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        var view: View? = null
        try {
            view = inflater.inflate(R.layout.splash, container, false)
        } catch (e: InflateException) {
            e.printStackTrace()
        }
        return view
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val handler = Handler()
        handler.postDelayed(Runnable {
            replaceFragment(Home())

        }, 3000)

    }

}
