package com.prisyazhnuy.streaming.ui_activity_test.auth

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import androidx.test.rule.ActivityTestRule
import com.prisyazhnuy.streaming.R
import com.prisyazhnuy.streaming.extensions.getAppString
import com.prisyazhnuy.streaming.extensions.getStringApp
import com.prisyazhnuy.streaming.extensions.getStringArray
import com.prisyazhnuy.streaming.network_module_test.*
import com.prisyazhnuy.streaming.preferences.PreferencesProvider
import com.prisyazhnuy.streaming.NPApp
import com.prisyazhnuy.streaming.ui.screens.auth.AuthActivity
import com.prisyazhnuy.streaming.ui_activity_test.invalidMaxLengthPasswords
import com.prisyazhnuy.streaming.ui_activity_test.invalidMinLengthPasswords
import com.prisyazhnuy.streaming.utils.NOTHING
import com.prisyazhnuy.streaming.utils.UITestsUtils.hasTextInputLayoutErrorText
import com.prisyazhnuy.streaming.utils.UITestsUtils.matchTextInputLayoutErrorText
import com.prisyazhnuy.streaming.utils.UITestsUtils.waitFor
import okhttp3.mockwebserver.MockWebServer
import org.hamcrest.Matchers.not
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith


@SuppressWarnings("all")
@RunWith(AndroidJUnit4::class)
@LargeTest
class SignInFragmentTest {

    companion object {
        private const val MIN_PASSWORD_LENGTH = 6
        private const val MAX_PASSWORD_LENGTH = 30
        private const val DEFAULT_DELAY_MS = 100L
        private const val SHORT_DELAY_MS = 500L
        private const val SIGNED_IN_SUCCESS = "Signed in"
    }

    private lateinit var mockServer: MockWebServer

    @Before
    fun clearData() {
        PreferencesProvider.clearData()
        NPApp.instance.getSession().run {
            accessToken = NOTHING
            refreshToken = NOTHING
        }
        mockServer = MockServer().initMockServer(RequestDispatcher())
    }

    @get:Rule
    var activityRule = ActivityTestRule(AuthActivity::class.java)

    @Test
    fun testSignIn() {
        activityRule.activity.openSignIn()
        onView(isRoot()).perform(waitFor(DEFAULT_DELAY_MS))
        //Check edittexts visibility
        onView(withId(R.id.etSignInEmail))
                .check(matches(withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE)))
        onView(withId(R.id.etSignInPassword))
                .check(matches(withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE)))
        onView(isRoot()).perform(waitFor(DEFAULT_DELAY_MS))
        //Check correct hints
        onView(withId(R.id.etSignInEmail))
                .check(ViewAssertions.matches(withHint(R.string.email)))
        onView(withId(R.id.etSignInPassword))
                .check(ViewAssertions.matches(withHint(R.string.password)))
        onView(isRoot()).perform(waitFor(DEFAULT_DELAY_MS))
        //Check click action
        onView(withId(R.id.etSignInEmail))
                .perform(ViewActions.click())
        onView(withId(R.id.etSignInEmail))
                .perform(ViewActions.pressImeActionButton())
        onView(withId(R.id.etSignInPassword))
                .check(matches(ViewMatchers.hasFocus()))
        onView(isRoot()).perform(waitFor(DEFAULT_DELAY_MS))
        //Click button when edittexts is empty
        onView(withId(R.id.bSignIn))
                .perform(ViewActions.click())
        onView(isRoot()).perform(waitFor(DEFAULT_DELAY_MS))
        onView(withId(R.id.tilSignInEmail))
                .check(matches(matchTextInputLayoutErrorText(getStringApp(R.string.email_is_empty))))
        onView(withId(R.id.tilSignInPassword))
                .check(matches(matchTextInputLayoutErrorText(getStringApp(R.string.password_is_empty))))
        onView(isRoot()).perform(waitFor(DEFAULT_DELAY_MS))
        //Test valid emails
        getStringArray(R.array.invalid_emails).forEach { email ->
            onView(withId(R.id.etSignInEmail)).perform(ViewActions.replaceText(email), ViewActions.closeSoftKeyboard())
            onView(withId(R.id.bSignIn)).perform(click())
            onView(withId(R.id.tilSignInEmail))
                    .check(matches(matchTextInputLayoutErrorText(getStringApp(R.string.email_is_invalid))))
            onView(isRoot()).perform(waitFor(DEFAULT_DELAY_MS))
        }

        //Test invalid emails
        getStringArray(R.array.valid_emails).forEach { email ->
            onView(withId(R.id.etSignInEmail)).perform(ViewActions.replaceText(email), ViewActions.closeSoftKeyboard())
            onView(withId(R.id.bSignIn)).perform(click())
            onView(withId(R.id.tilSignInEmail))
                    .check(matches(not(hasTextInputLayoutErrorText())))
        }

        //Test invalid minimal length passwords
        invalidMinLengthPasswords.forEach { password ->
            onView(withId(R.id.etSignInPassword)).perform(replaceText(password), closeSoftKeyboard())
            onView(withId(R.id.bSignIn)).perform(click())
            onView(withId(R.id.tilSignInPassword))
                    .check(matches(matchTextInputLayoutErrorText(
                            getAppString(R.string.password_min_length_error, MIN_PASSWORD_LENGTH))))
        }

        //Test invalid maximal length passwords=
        invalidMaxLengthPasswords.forEach { password ->
            onView(withId(R.id.etSignInPassword)).perform(ViewActions.replaceText(password), ViewActions.closeSoftKeyboard())
            onView(withId(R.id.bSignIn)).perform(ViewActions.click())
            onView(withId(R.id.tilSignInPassword))
                    .check(matches(matchTextInputLayoutErrorText(
                            getAppString(R.string.password_max_length_error, MAX_PASSWORD_LENGTH))))
        }
        onView(isRoot()).perform(waitFor(DEFAULT_DELAY_MS))

        //Set invalid email and valid password
        onView(withId(R.id.etSignInEmail)).perform(replaceText(getStringArray(R.array.invalid_emails).first()),
                closeSoftKeyboard())
        onView(withId(R.id.etSignInPassword)).perform(replaceText(USER_LOGIN_TEST_PASSWORD), closeSoftKeyboard())
        onView(withId(R.id.bSignIn)).perform(click())
        onView(withId(R.id.tilSignInEmail))
                .check(matches(matchTextInputLayoutErrorText(getStringApp(R.string.email_is_invalid))))
        onView(withId(R.id.tilSignInPassword))
                .check(matches(not(hasTextInputLayoutErrorText())))
        onView(isRoot()).perform(waitFor(DEFAULT_DELAY_MS))

        //Set valid email and invalid password
        onView(withId(R.id.etSignInEmail)).perform(replaceText(getStringArray(R.array.valid_emails).first()),
                closeSoftKeyboard())
        onView(withId(R.id.etSignInPassword)).perform(replaceText(invalidMinLengthPasswords.first()), closeSoftKeyboard())
        onView(withId(R.id.bSignIn)).perform(ViewActions.click())
        onView(withId(R.id.tilSignInPassword))
                .check(matches(matchTextInputLayoutErrorText(getAppString(R.string.password_min_length_error, MIN_PASSWORD_LENGTH))))
        onView(withId(R.id.tilSignInEmail))
                .check(matches(not(hasTextInputLayoutErrorText())))
        onView(isRoot()).perform(waitFor(DEFAULT_DELAY_MS))

        //Try sign in
        onView(withId(R.id.etSignInPassword)).perform(replaceText(USER_SIGN_UP_TEST_PASSWORD), closeSoftKeyboard())
        onView(withId(R.id.etSignInEmail)).perform(replaceText(USER_LOGIN_TEST_EMAIL), closeSoftKeyboard())
        onView(withId(R.id.bSignIn)).perform(click())
        onView(withId(R.id.tilSignInEmail))
                .check(matches(not(hasTextInputLayoutErrorText())))
        onView(withId(R.id.tilSignInPassword))
                .check(matches(not(hasTextInputLayoutErrorText())))
        onView(isRoot()).perform(waitFor(SHORT_DELAY_MS))
        onView(withText(SIGNED_IN_SUCCESS)).check(matches(isDisplayed()))
        onView(isRoot()).perform(waitFor(DEFAULT_DELAY_MS))
    }

    @After
    fun shutdownMockServer() {
        mockServer.shutdown()
    }
}