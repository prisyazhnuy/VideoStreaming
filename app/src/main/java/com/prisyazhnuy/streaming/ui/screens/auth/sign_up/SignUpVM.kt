package com.prisyazhnuy.streaming.ui.screens.auth.sign_up

import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.cleveroad.bootstrap.kotlin_validators.ValidatorsFactory
import com.prisyazhnuy.streaming.models.User
import com.prisyazhnuy.streaming.providers.ProviderInjector
import com.prisyazhnuy.streaming.ui.base.BaseViewModel
import com.prisyazhnuy.streaming.utils.validation.ValidationField
import com.prisyazhnuy.streaming.utils.validation.ValidationResponseWrapper
import com.prisyazhnuy.streaming.utils.validation.ValidationWrapper

class SignUpVM(application: Application) : BaseViewModel(application) {

    private val firstNameValidator = ValidatorsFactory.getFirstNameValidator(application)
    private val lastNameValidator = ValidatorsFactory.getLastNameValidator(application)
    private val emailValidator = ValidatorsFactory.getEmailValidator(application)
    private val matchPasswordsValidator = ValidatorsFactory.getMatchPasswordValidator(application)
    private val passwordValidator = ValidatorsFactory.getPasswordValidator(application)

    private val accountProvider by lazy { ProviderInjector.getAccountProvider() }

    val validationLD = MutableLiveData<ValidationResponseWrapper>()
    val registrationLD = MutableLiveData<User>()

    fun signUp(fName: String, lName: String, email: String, password: String, confirmPassword: String) =
            validate(ValidationWrapper(email, password, confirmPassword, fName, lName)).apply {
                takeIf { this }
                        ?.let { registration(fName, lName, email, password, confirmPassword) }
            }

    private fun validate(validationWrapper: ValidationWrapper): Boolean = validationWrapper.run {
        validateFirstName(firstName) and validateLastName(lastName) and
                validateEmail(email) and validatePassword(password) and validateMatchPasswords(password, confirmPassword)
    }

    private fun validateFirstName(fName: String): Boolean =
            firstNameValidator.validate(fName).run {
                validationLD.value = ValidationResponseWrapper(this, ValidationField.FIRST_NAME)
                isValid
            }

    private fun validateLastName(lName: String): Boolean =
            lastNameValidator.validate(lName).run {
                validationLD.value = ValidationResponseWrapper(this, ValidationField.LAST_NAME)
                isValid
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


    private fun validateMatchPasswords(password: String, confirmPassword: String): Boolean =
            matchPasswordsValidator.validate(password, confirmPassword).run {
                validationLD.value = ValidationResponseWrapper(this, ValidationField.CONFIRM_PASSWORD)
                isValid
            }

    private fun registration(fName: String, lName: String, email: String, password: String, confirmPassword: String) {
        accountProvider.register(fName, lName, email, password, confirmPassword)
                .doAsync(registrationLD)
    }
}
