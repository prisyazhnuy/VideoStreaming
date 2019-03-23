package com.prisyazhnuy.streaming.ui_activity_test.auth

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.action.ViewActions.*
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
import com.prisyazhnuy.streaming.VSApp
import com.prisyazhnuy.streaming.ui.screens.auth.AuthActivity
import com.prisyazhnuy.streaming.ui_activity_test.invalidMaxLengthPasswords
import com.prisyazhnuy.streaming.ui_activity_test.invalidNames
import com.prisyazhnuy.streaming.ui_activity_test.validNames
import com.prisyazhnuy.streaming.utils.*
import com.prisyazhnuy.streaming.utils.UITestsUtils.hasTextInputLayoutErrorText
import com.prisyazhnuy.streaming.utils.UITestsUtils.matchTextInputLayoutErrorText
import com.prisyazhnuy.streaming.utils.UITestsUtils.nestedScrollTo
import com.prisyazhnuy.streaming.utils.UITestsUtils.waitFor
import okhttp3.mockwebserver.MockWebServer
import org.hamcrest.Matchers.allOf
import org.hamcrest.Matchers.not
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@SuppressWarnings("all")
@RunWith(AndroidJUnit4::class)
@LargeTest
class SignUpFragmentTest {

    companion object {
        private const val TAG = "SignInFragmentTest"
        private const val MAX_PASSWORD_LENGTH = 30
        private const val DEFAULT_DELAY_MS = 100L
        private const val SNACKBAR_DELAY_MS = 3000L
        private const val SHORT_DELAY_MS = 500L
        private const val SIGNED_UP_SUCCESS = "Signed up"
    }

    private lateinit var mockServer: MockWebServer

    @Before
    fun clearData() {
        PreferencesProvider.clearData()
        VSApp.instance.getSession().run {
            accessToken = NOTHING
            refreshToken = NOTHING
        }
        mockServer = MockServer().initMockServer(RequestDispatcher())
    }

    @get:Rule
    var activityRule = ActivityTestRule(AuthActivity::class.java)

    @Test
    fun testSignIn() {
        //Check edittexts visibility
        onView(allOf(withId(R.id.etSignUpFirstName), withId(R.id.etSignUpLastName),
                withId(R.id.etSignUpEmail), withId(R.id.etSignUpEmail), withId(R.id.etSignUpPassword),
                withId(R.id.etSignUpConfPassword), isDisplayed()))
        onView(isRoot()).perform(waitFor(DEFAULT_DELAY_MS))

        //Check correct hints
        onView(withId(R.id.etSignUpFirstName))
                .check(matches(withHint(R.string.first_name)))
        onView(withId(R.id.etSignUpLastName))
                .check(matches(withHint(R.string.last_name)))
        onView(withId(R.id.etSignUpEmail))
                .check(matches(withHint(R.string.email)))
        onView(withId(R.id.etSignUpPassword))
                .check(matches(withHint(R.string.password)))
        onView(withId(R.id.etSignUpConfPassword))
                .check(matches(withHint(R.string.confirm_password)))
        onView(isRoot()).perform(waitFor(DEFAULT_DELAY_MS))

        //Check ime click action
        onView(withId(R.id.etSignUpFirstName))
                .perform(ViewActions.pressImeActionButton())
        onView(withId(R.id.etSignUpLastName))
                .check(matches(ViewMatchers.hasFocus()))

        onView(withId(R.id.etSignUpLastName))
                .perform(ViewActions.pressImeActionButton())
        onView(withId(R.id.etSignUpEmail))
                .check(matches(ViewMatchers.hasFocus()))

        onView(withId(R.id.etSignUpEmail))
                .perform(ViewActions.pressImeActionButton())
        onView(withId(R.id.etSignUpPassword))
                .check(matches(ViewMatchers.hasFocus()))

        onView(withId(R.id.etSignUpPassword))
                .perform(ViewActions.pressImeActionButton())
        onView(withId(R.id.etSignUpConfPassword))
                .check(matches(ViewMatchers.hasFocus()))
        onView(isRoot()).perform(waitFor(DEFAULT_DELAY_MS))

        //Check empty views
        onView(withId(R.id.bSignUp))
                .perform(ViewActions.click())
        onView(isRoot()).perform(waitFor(DEFAULT_DELAY_MS))
        onView(withId(R.id.tilSignUpFirstName))
                .check(matches(matchTextInputLayoutErrorText(getStringApp(R.string.first_name_is_empty))))
        onView(withId(R.id.tilSignUpLastName))
                .check(matches(matchTextInputLayoutErrorText(getStringApp(R.string.last_name_is_empty))))
        onView(withId(R.id.tilSignUpEmail))
                .check(matches(matchTextInputLayoutErrorText(getStringApp(R.string.email_is_empty))))
        onView(withId(R.id.tilSignUpPassword))
                .check(matches(matchTextInputLayoutErrorText(getStringApp(R.string.password_is_empty))))
        onView(withId(R.id.tilSignUpConfPassword))
                .check(matches(matchTextInputLayoutErrorText(getStringApp(R.string.password_is_empty))))
        onView(isRoot()).perform(waitFor(DEFAULT_DELAY_MS))

        //Check valid first names
        validNames.forEach { name ->
            onView(withId(R.id.etSignUpFirstName)).perform(ViewActions.replaceText(name))
            onView(withId(R.id.bSignUp)).perform(nestedScrollTo(), click())
            onView(withId(R.id.tilSignUpFirstName))
                    .check(matches(not(hasTextInputLayoutErrorText())))
        }
        onView(isRoot()).perform(waitFor(DEFAULT_DELAY_MS))

        //Check invalid first names
        invalidNames.forEach { name ->
            onView(withId(R.id.etSignUpFirstName)).perform(replaceText(name), closeSoftKeyboard())
            onView(withId(R.id.bSignUp)).perform(nestedScrollTo(), click())
            onView(withId(R.id.tilSignUpFirstName))
                    .check(matches(matchTextInputLayoutErrorText(getStringApp(R.string.name_is_too_long))))
        }
        onView(isRoot()).perform(waitFor(DEFAULT_DELAY_MS))

        //Check valid emails
        getStringArray(R.array.valid_emails).forEach { email ->
            onView(withId(R.id.etSignUpEmail)).perform(replaceText(email), closeSoftKeyboard())
            onView(withId(R.id.bSignUp)).perform(nestedScrollTo(), click())
            onView(withId(R.id.tilSignUpEmail))
                    .check(matches(not(hasTextInputLayoutErrorText())))
        }

        //Check invalid emails
        getStringArray(R.array.invalid_emails).forEach { email ->
            onView(withId(R.id.etSignUpEmail)).perform(replaceText(email), closeSoftKeyboard())
            onView(withId(R.id.bSignUp)).perform(nestedScrollTo(), click())
            onView(withId(R.id.tilSignUpEmail))
                    .check(matches(matchTextInputLayoutErrorText(getStringApp(R.string.email_is_invalid))))
        }
        //Check invalid passwords
        invalidMaxLengthPasswords.forEach { password ->
            onView(withId(R.id.etSignUpPassword)).perform(ViewActions.replaceText(password), ViewActions.closeSoftKeyboard())
            onView(withId(R.id.bSignUp)).perform(nestedScrollTo(), click())
            onView(withId(R.id.tilSignUpPassword))
                    .check(matches(matchTextInputLayoutErrorText(
                            getAppString(R.string.password_max_length_error, MAX_PASSWORD_LENGTH))))
            onView(isRoot()).perform(waitFor(DEFAULT_DELAY_MS))
        }

        //Check confirm passwords
        onView(withId(R.id.etSignUpPassword)).perform(ViewActions.replaceText(EMPTY_STRING), ViewActions.closeSoftKeyboard())
        onView(withId(R.id.etSignUpConfPassword)).perform(ViewActions.replaceText(USER_SIGN_UP_TEST_PASSWORD), ViewActions.closeSoftKeyboard())
        onView(withId(R.id.bSignUp)).perform(nestedScrollTo(), click())
        onView(withId(R.id.tilSignUpConfPassword))
                .check(matches(matchTextInputLayoutErrorText(
                        getAppString(R.string.password_is_empty))))
        onView(isRoot()).perform(waitFor(DEFAULT_DELAY_MS))

        //Check confirm passwords not match
        onView(withId(R.id.etSignUpPassword)).perform(replaceText(USER_SIGN_UP_TEST_PASSWORD + TAG), closeSoftKeyboard())
        onView(withId(R.id.etSignUpConfPassword)).perform(replaceText(USER_SIGN_UP_TEST_PASSWORD), closeSoftKeyboard())
        onView(withId(R.id.bSignUp)).perform(nestedScrollTo(), click())
        onView(withId(R.id.tilSignUpConfPassword))
                .check(matches(matchTextInputLayoutErrorText(getAppString(R.string.passwords_dont_match))))
        onView(isRoot()).perform(waitFor(DEFAULT_DELAY_MS))

        //Check confirm passwords match
        onView(withId(R.id.etSignUpPassword)).perform(replaceText(USER_SIGN_UP_TEST_PASSWORD), closeSoftKeyboard())
        onView(withId(R.id.etSignUpConfPassword)).perform(replaceText(USER_SIGN_UP_TEST_PASSWORD), closeSoftKeyboard())
        onView(withId(R.id.bSignUp)).perform(nestedScrollTo(), click())
        onView(withId(R.id.tilSignUpConfPassword)).check(matches(not(hasTextInputLayoutErrorText())))
        onView(isRoot()).perform(waitFor(DEFAULT_DELAY_MS))

        //Check sign up
        onView(withId(R.id.etSignUpPassword)).perform(replaceText(USER_SIGN_UP_TEST_PASSWORD), closeSoftKeyboard())
        onView(withId(R.id.etSignUpConfPassword)).perform(replaceText(USER_SIGN_UP_TEST_PASSWORD), closeSoftKeyboard())
        onView(withId(R.id.etSignUpEmail)).perform(replaceText(USER_SIGN_UP_TEST_EMAIL), closeSoftKeyboard())
        onView(withId(R.id.etSignUpFirstName)).perform(replaceText(USER_SIGN_UP_TEST_FIRSTNAME), closeSoftKeyboard())
        onView(withId(R.id.etSignUpLastName)).perform(replaceText(USER_SIGN_UP_TEST_LASTNAME), closeSoftKeyboard())
        onView(withId(R.id.bSignUp)).perform(nestedScrollTo(), click())
        onView(isRoot()).perform(waitFor(SHORT_DELAY_MS))
        onView(allOf(withId(com.google.android.material.R.id.snackbar_text),
                withText(SIGNED_UP_SUCCESS))).check(matches(isDisplayed()))
        //Delay after snack bar
        onView(isRoot()).perform(waitFor(SNACKBAR_DELAY_MS))

        onView(withText(R.string.sign_in)).perform(nestedScrollTo(), click())
        onView(isRoot()).perform(waitFor(DEFAULT_DELAY_MS))
        onView(withId(R.id.tvRegSignIn)).check(matches(isDisplayed()))
        onView(withId(R.id.tvSignUp)).check(matches(isDisplayed()))

        onView(isRoot()).perform(waitFor(DEFAULT_DELAY_MS))
    }

    @After
    fun shutdownMockServer() {
        mockServer.shutdown()
    }
}