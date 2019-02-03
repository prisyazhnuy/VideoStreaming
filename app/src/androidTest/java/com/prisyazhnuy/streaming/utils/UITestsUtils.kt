package com.prisyazhnuy.streaming.utils

import android.view.View
import android.view.ViewParent
import android.widget.FrameLayout
import androidx.core.widget.NestedScrollView
import androidx.test.espresso.PerformException
import androidx.test.espresso.UiController
import androidx.test.espresso.ViewAction
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.espresso.util.HumanReadables
import com.google.android.material.textfield.TextInputLayout
import org.hamcrest.Description
import org.hamcrest.Matchers
import org.hamcrest.TypeSafeMatcher


object UITestsUtils {

    fun matchTextInputLayoutErrorText(expectedErrorText: String) = object : TypeSafeMatcher<View>() {
        override fun matchesSafely(view: View) =
                (view as? TextInputLayout)?.let {
                    it.error == expectedErrorText
                } ?: false

        override fun describeTo(description: Description) = Unit
    }

    fun hasTextInputLayoutErrorText() = object : TypeSafeMatcher<View>() {
        override fun matchesSafely(view: View) = (view as? TextInputLayout)?.error?.isNotEmpty() == true
        override fun describeTo(description: Description) = Unit
    }

    /**
     * Perform action of waiting for a specific time.
     */
    fun waitFor(millis: Long) = object : ViewAction {
        override fun getConstraints() = isRoot()

        override fun getDescription() = "Wait for $millis milliseconds."

        override fun perform(uiController: UiController, view: View) {
            uiController.loopMainThreadForAtLeast(millis)
        }
    }

    fun nestedScrollTo() = object : ViewAction {

        override fun getConstraints() = Matchers.allOf(
                isDescendantOfA(isAssignableFrom(NestedScrollView::class.java)),
                withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE))

        override fun getDescription() = "View is not NestedScrollView"

        override fun perform(uiController: UiController, view: View) {
            try {
                (findFirstParentLayoutOfClass(view, NestedScrollView::class.java) as NestedScrollView?)?.apply {
                    scrollTo(0, view.top)
                } ?: throw Exception("Unable to find NestedScrollView parent.")
            } catch (e: Exception) {
                throw PerformException.Builder()
                        .withActionDescription(this.description)
                        .withViewDescription(HumanReadables.describe(view))
                        .withCause(e)
                        .build()
            }

            uiController.loopMainThreadUntilIdle()
        }
    }

    private fun findFirstParentLayoutOfClass(view: View, parentClass: Class<out View>): View? {
        var parent: ViewParent? = FrameLayout(view.context)
        var incrementView: ViewParent? = null
        var i = 0
        while (parent != null && parent.javaClass != parentClass) {
            parent = if (i == 0) {
                findParent(view)
            } else {
                incrementView?.let { findParent(it) }
            }
            incrementView = parent
            i++
        }
        return parent as View?
    }

    private fun findParent(view: View) = view.parent

    private fun findParent(view: ViewParent) = view.parent
}