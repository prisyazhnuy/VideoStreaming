package com.prisyazhnuy.streaming.network_module_test

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.cleveroad.bootstrap.kotlin_core.utils.ioToMainSingle
import com.prisyazhnuy.streaming.NPApp
import com.prisyazhnuy.streaming.extensions.blockingObserve
import com.prisyazhnuy.streaming.models.User
import com.prisyazhnuy.streaming.preferences.PreferencesProvider
import com.prisyazhnuy.streaming.providers.ProviderInjector
import com.prisyazhnuy.streaming.ui.screens.auth.sign_in.SignInVM
import com.prisyazhnuy.streaming.ui.screens.auth.sign_up.SignUpVM
import com.prisyazhnuy.streaming.utils.EMPTY_STRING
import com.prisyazhnuy.streaming.utils.NOTHING
import io.reactivex.observers.TestObserver
import junit.framework.TestCase.*
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import java.util.concurrent.TimeUnit


@RunWith(AndroidJUnit4::class)
class ServerClientSuccessTest {

    companion object {
        private const val TWO_SEC = 2L
        private const val EMPTY_ERRORS_COUNT = 0
        private const val MIN_VALUES_COUNT = 1
    }

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var mockServer: MockWebServer

    private val signUpVM by lazy { SignUpVM(NPApp.instance) }
    private val signInVM by lazy { SignInVM(NPApp.instance) }
    private val testUserObserver = TestObserver<User>()
    private val testUnitObserver = TestObserver<Unit>()

    @Before
    fun createMockServer() {
        mockServer = MockServer().initMockServer(RequestDispatcher())
    }

    @Test
    fun testRegisterUserVMSuccess() {
        PreferencesProvider.clearData()
        NPApp.instance.getSession().run {
            accessToken = NOTHING
            refreshToken = NOTHING
        }

        signUpVM.signUp(USER_SIGN_UP_TEST_FIRSTNAME,
                USER_SIGN_UP_TEST_LASTNAME,
                USER_SIGN_UP_TEST_EMAIL,
                USER_SIGN_UP_TEST_PASSWORD,
                USER_SIGN_UP_TEST_PASSWORD)

        val registrationValue = signUpVM.registrationLD.blockingObserve()

        assertNotNull(registrationValue)

        PreferencesProvider.run {
            assertEquals(EMPTY_STRING, token)
            assertEquals(EMPTY_STRING, refreshToken)
        }

        NPApp.instance.getSession().run {
            assertNull(accessToken)
            assertNull(refreshToken)
        }

        assertEquals(EndpointPath.ACCOUNT.path, mockServer.takeRequest().path)
    }

    @Test
    fun testLoginSuccess() {

        PreferencesProvider.clearData()

        ProviderInjector.getAccountProvider()
                .login(USER_LOGIN_TEST_EMAIL, USER_LOGIN_TEST_PASSWORD)
                .compose(ioToMainSingle())
                .subscribe(testUserObserver)
        testUserObserver.awaitTerminalEvent(TWO_SEC, TimeUnit.SECONDS)

        testUserObserver.run {
            assertComplete()
            assertEquals(MIN_VALUES_COUNT, values().size)
            assertEquals(EMPTY_ERRORS_COUNT, errorCount())
        }

        PreferencesProvider.run {
            assertEquals(EMPTY_STRING, token)
            assertEquals(EMPTY_STRING, refreshToken)
        }

        NPApp.instance.getSession().run {
            assertNotNull(accessToken)
            assertNotNull(refreshToken)
        }

        val loginRequest = mockServer.takeRequest()
        assertEquals(EndpointPath.LOGIN.path, loginRequest.path)
    }

    @Test
    fun testLoginVMSuccess() {
        PreferencesProvider.clearData()

        signInVM.signIn(USER_LOGIN_TEST_EMAIL, USER_LOGIN_TEST_PASSWORD)

        val authorizationValue = signInVM.authorizationLD.blockingObserve()

        assertNotNull(authorizationValue)

        PreferencesProvider.run {
            assertEquals(EMPTY_STRING, token)
            assertEquals(EMPTY_STRING, refreshToken)
        }

        NPApp.instance.getSession().run {
            assertNotNull(accessToken)
            assertNotNull(refreshToken)
        }

        val loginRequest = mockServer.takeRequest()
        assertEquals(EndpointPath.LOGIN.path, loginRequest.path)
    }

    @Test
    fun testLogOutSuccess() {
        ProviderInjector.getAccountProvider().logout()
                .compose(ioToMainSingle())
                .subscribe(testUnitObserver)
        testUnitObserver.awaitTerminalEvent(TWO_SEC, TimeUnit.SECONDS)

        testUnitObserver.run {
            assertEquals(MIN_VALUES_COUNT, values().size)
            assertEquals(EMPTY_ERRORS_COUNT, errorCount())
        }

        val logoutRequest = mockServer.takeRequest()
        assertEquals(EndpointPath.LOGOUT.path, logoutRequest.path)
    }

    @After
    fun shutdownMockServer() {
        mockServer.shutdown()
    }
}
