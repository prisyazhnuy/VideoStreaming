package com.prisyazhnuy.streaming.validation_test

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.prisyazhnuy.streaming.VSApp
import com.prisyazhnuy.streaming.ui.screens.auth.sign_up.SignUpVM
import junit.framework.TestCase.assertEquals
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class SignUpValidationTest {

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    private val signUpVM by lazy { SignUpVM(VSApp.instance) }

    @Test
    fun testCorrectEmail() {
        val actual = signUpVM.signUp(
                "first",
                "last",
                "email@ad.as",
                "1234567",
                "1234567")
        assertEquals(true, actual)
    }

    @Test
    fun testIncorrectEmail() {
        val actual = signUpVM.signUp(
                "first",
                "last",
                "email@ad",
                "1234567",
                "1234567")
        assertEquals(false, actual)
    }

    @Test
    fun testEmptyEmail() {
        val actual = signUpVM.signUp(
                "first",
                "last",
                "",
                "1234567",
                "1234567")
        assertEquals(false, actual)
    }

    @Test
    fun testCorrectPassword() {
        val actual = signUpVM.signUp(
                "first",
                "last",
                "email@ad.sdf",
                "1234567",
                "1234567")
        assertEquals(true, actual)
    }

    @Test
    fun testIncorrectPassword() {
        val actual = signUpVM.signUp(
                "first",
                "last",
                "email@ad.sdf",
                "123",
                "4567")
        assertEquals(false, actual)
    }

    @Test
    fun testEmptyPassword() {
        val actual = signUpVM.signUp(
                "first",
                "last",
                "email@ad",
                "",
                "")
        assertEquals(false, actual)
    }

    @Test
    fun testCorrectFirstName() {
        val actual = signUpVM.signUp(
                "first",
                "last",
                "email@ad.sdf",
                "1234567",
                "1234567")
        assertEquals(true, actual)
    }

    @Test
    fun testEmptyFirstName() {
        val actual = signUpVM.signUp(
                "",
                "last",
                "email@ad.sdf",
                "1234567",
                "1234567")
        assertEquals(false, actual)
    }

    @Test
    fun testCorrectLastName() {
        val actual = signUpVM.signUp(
                "first",
                "last",
                "email@ad.sdf",
                "1234567",
                "1234567")
        assertEquals(true, actual)
    }

    @Test
    fun testEmptyLastName() {
        val actual = signUpVM.signUp(
                "first",
                "",
                "email@ad.sdf",
                "1234567",
                "1234567")
        assertEquals(false, actual)
    }
}