package com.prisyazhnuy.streaming.ui_activity_test.splash

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.rule.ActivityTestRule
import com.prisyazhnuy.streaming.R
import com.prisyazhnuy.streaming.preferences.PreferencesProvider
import com.prisyazhnuy.streaming.NPApp
import com.prisyazhnuy.streaming.ui.screens.splash.SplashActivity
import com.prisyazhnuy.streaming.utils.NOTHING
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith


@SuppressWarnings("all")
@RunWith(AndroidJUnit4::class)
class SplashActivityTest {

    @Before
    fun clearData() {
        PreferencesProvider.clearData()
        NPApp.instance.getSession().run {
            accessToken = NOTHING
            refreshToken = NOTHING
        }
    }

    @get:Rule
    var activityRule = ActivityTestRule(SplashActivity::class.java)

    @Test
    fun splashActivityTest() {
        onView(ViewMatchers.withId(R.id.container))
                .check(matches(ViewMatchers.withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE)))
    }
}
