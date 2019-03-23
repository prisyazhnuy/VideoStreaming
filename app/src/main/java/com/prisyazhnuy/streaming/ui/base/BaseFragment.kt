package com.prisyazhnuy.streaming.ui.base

import androidx.fragment.app.Fragment
import com.cleveroad.bootstrap.kotlin_core.ui.BaseLifecycleFragment
import com.cleveroad.bootstrap.kotlin_core.ui.BaseLifecycleViewModel
import com.prisyazhnuy.streaming.BuildConfig
import com.prisyazhnuy.streaming.R

abstract class BaseFragment<T : BaseLifecycleViewModel> : BaseLifecycleFragment<T>() {

    abstract val containerId: Int

    override var endpoint = BuildConfig.ENDPOINT

    override var versionName = BuildConfig.VERSION_NAME

    override fun getVersionsLayoutId() = NO_TOOLBAR

    override fun getEndPointTextViewId() = NO_TOOLBAR

    override fun getVersionsTextViewId() = NO_TOOLBAR

    override fun isDebug() = BuildConfig.DEBUG

    override fun showBlockBackAlert() {
        // override this method if you need to show a warning when going to action back
    }

    fun replaceFragment(fragment: Fragment, needToAddToBackStack: Boolean = true) {
        val name = fragment.javaClass.simpleName
        with(childFragmentManager.beginTransaction()) {
            replace(containerId, fragment, name)
            if (needToAddToBackStack) {
                addToBackStack(name)
            }
            commit()
        }
    }
}