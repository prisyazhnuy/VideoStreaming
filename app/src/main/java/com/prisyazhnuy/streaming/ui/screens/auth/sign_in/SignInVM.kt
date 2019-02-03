package com.prisyazhnuy.streaming.ui.screens.auth.sign_in

import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.cleveroad.bootstrap.kotlin_validators.ValidatorsFactory
import com.prisyazhnuy.streaming.models.User
import com.prisyazhnuy.streaming.providers.ProviderInjector
import com.prisyazhnuy.streaming.ui.base.BaseViewModel
import com.prisyazhnuy.streaming.utils.validation.ValidationField
import com.prisyazhnuy.streaming.utils.validation.ValidationResponseWrapper
import com.prisyazhnuy.streaming.utils.validation.ValidationWrapper

class SignInVM(application: Application) : BaseViewModel(application) {

    private val emailValidator = ValidatorsFactory.getEmailValidator(application)
    private val passwordValidator = ValidatorsFactory.getPasswordValidator(application)

    val authorizationLD = MutableLiveData<User>()

    val validationLD = MutableLiveData<ValidationResponseWrapper>()

    private val accountProvider by lazy { ProviderInjector.getAccountProvider() }

    fun signIn(email: String, password: String) =
            validate(ValidationWrapper(email, password)).apply {
                takeIf { this }
                        ?.let { login(email, password) }
            }

    private fun validate(validationWrapper: ValidationWrapper): Boolean = validationWrapper.run {
        validateEmail(email) and validatePassword(password)
    }

    private fun validateEmail(email: String): Boolean =
            emailValidator.validate(email).run {
                validationLD.value = ValidationResponseWrapper(this, ValidationField.EMAIL)
                isValid
            }

    private fun validatePassword(password: String): Boolean =
            passwordValidator.validate(password).run {
                validationLD.value = ValidationResponseWrapper(this, ValidationField.PASSWORD)
                isValid
            }

    private fun login(email: String, password: String) {
        accountProvider.login(email, password)
                .doAsync(authorizationLD)
    }
}