package ru.androidschool.intensiv

import android.view.View
import androidx.annotation.LayoutRes
import androidx.fragment.app.Fragment

abstract class BaseStateFragment(@LayoutRes layout: Int) : Fragment(layout) {

    abstract fun getProgressBar(): View

    fun showProgressBar() {
        getProgressBar().visibility = View.VISIBLE
    }

    fun hideProgressBar() {
        getProgressBar().visibility = View.GONE
    }
}
