package com.prisyazhnuy.streaming.network_module_test

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.cleveroad.bootstrap.kotlin_core.utils.ioToMainSingle
import com.prisyazhnuy.streaming.extensions.getMockResponse
import com.prisyazhnuy.streaming.models.User
import com.prisyazhnuy.streaming.preferences.PreferencesProvider
import com.prisyazhnuy.streaming.providers.ProviderInjector
import io.reactivex.observers.TestObserver
import junit.framework.TestCase.assertEquals
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import java.net.HttpURLConnection.HTTP_BAD_REQUEST
import java.net.HttpURLConnection.HTTP_UNAUTHORIZED
import java.util.concurrent.TimeUnit


@RunWith(AndroidJUnit4::class)
class RefreshTokenTest {

    companion object {
        private const val TWO_SEC = 2L
        private const val EMPTY_ERRORS_SIZE = 0
        private const val MIN_ERROR_COUNT = 1
    }

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var mockServer: MockWebServer

    private val testUserObserver = TestObserver<User>()

    @Before
    fun createMockServer() {
        PreferencesProvider.token = MOCK_SERVER_TOKEN
        mockServer = MockServer().initMockServer(HTTP_UNAUTHORIZED, "unit.json")
    }

    @Test
    fun testRefreshToken() {
        logIn()
        testUserObserver.awaitTerminalEvent(TWO_SEC, TimeUnit.SECONDS)
        testUserObserver.run {
            assertNotComplete()
            assertEquals(EMPTY_ERRORS_SIZE, values().size)
            assertEquals(EMPTY_ERRORS_SIZE, errorCount())
        }

        val loginRequest = mockServer.takeRequest()
        val refreshTokenRequest = mockServer.takeRequest()
        assertEquals(EndpointPath.LOGIN.path, loginRequest.path)
        assertEquals(EndpointPath.LOGIN.method, loginRequest.method)
        assertEquals(EndpointPath.REFRESH_TOKEN.path, refreshTokenRequest.path)
        assertEquals(EndpointPath.REFRESH_TOKEN.method, refreshTokenRequest.method)
    }

    @Test
    fun testRefreshTokenError() {
        mockServer.enqueue(getMockResponse(HTTP_BAD_REQUEST, "unit.json"))
        logIn()
        testUserObserver.awaitTerminalEvent(TWO_SEC, TimeUnit.SECONDS)

        testUserObserver.run {
            assertNotComplete()
            assertEquals(EMPTY_ERRORS_SIZE, values().size)
            assertEquals(MIN_ERROR_COUNT, errorCount())
        }

        val loginRequest = mockServer.takeRequest()
        val refreshTokenRequest = mockServer.takeRequest()
        assertEquals(EndpointPath.LOGIN.path, loginRequest.path)
        assertEquals(EndpointPath.LOGIN.method, loginRequest.method)
        assertEquals(EndpointPath.REFRESH_TOKEN.path, refreshTokenRequest.path)
        assertEquals(EndpointPath.REFRESH_TOKEN.method, refreshTokenRequest.method)
    }

    @After
    fun shutdownMockServer() {
        PreferencesProvider.clearData()
        mockServer.shutdown()
    }

    private fun logIn() {
        ProviderInjector.getAccountProvider()
                .login(USER_LOGIN_TEST_EMAIL, USER_LOGIN_TEST_PASSWORD)
                .compose(ioToMainSingle())
                .subscribe(testUserObserver)
    }
}
