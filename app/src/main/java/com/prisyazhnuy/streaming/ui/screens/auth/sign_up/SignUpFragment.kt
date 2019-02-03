package com.prisyazhnuy.streaming.ui.screens.auth.sign_up

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import com.cleveroad.bootstrap.kotlin_ext.setClickListeners
import com.prisyazhnuy.streaming.R
import com.prisyazhnuy.streaming.extensions.showTextInputError
import com.prisyazhnuy.streaming.extensions.text
import com.prisyazhnuy.streaming.models.User
import com.prisyazhnuy.streaming.ui.base.BaseFragment
import com.prisyazhnuy.streaming.ui.listeners.HideErrorTextWatcher
import com.prisyazhnuy.streaming.utils.bindInterfaceOrThrow
import com.prisyazhnuy.streaming.utils.validation.ValidationField.*
import com.prisyazhnuy.streaming.utils.validation.ValidationResponseWrapper
import kotlinx.android.synthetic.main.fragment_sign_up.*

@Suppress("DEPRECATION")
class SignUpFragment : BaseFragment<SignUpVM>(),
        View.OnClickListener {

    companion object {
        fun newInstance() = SignUpFragment().apply {
            arguments = Bundle()
        }
    }

    override val layoutId = R.layout.fragment_sign_up

    override val viewModelClass = SignUpVM::class.java

    private var callback: SignUpCallback? = null

    private val validationObserver = Observer<ValidationResponseWrapper> {
        when (it.field) {
            FIRST_NAME -> tilSignUpFirstName.showTextInputError(it.response)
            LAST_NAME -> tilSignUpLastName.showTextInputError(it.response)
            EMAIL -> tilSignUpEmail.showTextInputError(it.response)
            PASSWORD -> tilSignUpPassword.showTextInputError(it.response)
            CONFIRM_PASSWORD -> tilSignUpConfPassword.showTextInputError(it.response)
        }
    }

    private val registrationObserver = Observer<User> {
        showSnackBar("Signed up")
    }

    override fun getScreenTitle() = NO_TITLE

    override fun getToolbarId() = NO_TOOLBAR

    override fun hasToolbar() = false

    override fun hasVersions() = true

    override fun observeLiveData(viewModel: SignUpVM) {
        with(viewModel) {
            validationLD.observe(this@SignUpFragment, validationObserver)
            registrationLD.observe(this@SignUpFragment, registrationObserver)
        }
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        callback = bindInterfaceOrThrow<SignUpCallback>(context)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) =
            super.onViewCreated(view, savedInstanceState).also { setupUi() }

    override fun onDetach() {
        callback = null
        super.onDetach()
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.tvSignIn -> callback?.openSignIn()
            R.id.bSignUp -> viewModel.signUp(etSignUpFirstName.text(),
                    etSignUpLastName.text(),
                    etSignUpEmail.text(),
                    etSignUpPassword.text(),
                    etSignUpConfPassword.text())
        }
    }

    private fun setupUi() {
        setClickListeners(tvSignIn, bSignUp)
        etSignUpEmail.addTextWatcher(HideErrorTextWatcher(tilSignUpEmail))
        etSignUpConfPassword.addTextWatcher(HideErrorTextWatcher(tilSignUpConfPassword))
        etSignUpFirstName.addTextWatcher(HideErrorTextWatcher(tilSignUpFirstName))
        etSignUpLastName.addTextWatcher(HideErrorTextWatcher(tilSignUpLastName))
    }
}
