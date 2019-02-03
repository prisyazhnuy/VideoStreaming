package com.prisyazhnuy.streaming.validation_test

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.prisyazhnuy.streaming.NPApp
import com.prisyazhnuy.streaming.ui.screens.auth.sign_in.SignInVM
import junit.framework.TestCase.assertEquals
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class SignInValidationTest {
    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    private val signInVM by lazy { SignInVM(NPApp.instance) }

    @Test
    fun testCorrectEmail() {
        val actual = signInVM.signIn("email@ad.as", "1234567")
        assertEquals(true, actual)
    }

    @Test
    fun testIncorrectEmail() {
        val actual = signInVM.signIn("email@ad", "1234567")
        assertEquals(false, actual)
    }

    @Test
    fun testEmptyEmail() {
        val actual = signInVM.signIn("", "1234567")
        assertEquals(false, actual)
    }

    @Test
    fun testCorrectPassword() {
        val actual = signInVM.signIn("email@ad.sdf", "1234567")
        assertEquals(true, actual)
    }

    @Test
    fun testIncorrectPassword() {
        val actual = signInVM.signIn("email@ad.sdf", "123")
        assertEquals(false, actual)
    }

    @Test
    fun testEmptyPassword() {
        val actual = signInVM.signIn("email@ad.com", "")
        assertEquals(false, actual)
    }
}