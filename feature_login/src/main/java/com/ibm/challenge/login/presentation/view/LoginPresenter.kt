package com.ibm.challenge.login.presentation.view

import com.ibm.challenge.core.Navigator
import com.ibm.challenge.core.mvp.BasePresenter
import com.ibm.challenge.domain.core.CacheHelper
import com.ibm.challenge.domain.interactos.cache.GetCacheObject
import com.ibm.challenge.domain.interactos.cache.PutCacheObject
import com.ibm.challenge.login.core.PresentationModelMapper
import com.ibm.challenge.login.domain.exceptions.InvalidLoginException
import com.ibm.challenge.login.domain.interactors.*
import com.ibm.challenge.login.domain.model.LoginResponseDomain
import com.ibm.challenge.login.domain.model.UserAccountDomain
import com.ibm.challenge.presentation.model.login.UserAccountModel

class LoginPresenter(private val navigator: Navigator,
                     private val validateCpf: ValidateCpf,
                     private val validateEmail: ValidateEmail,
                     private val validatePassword: ValidatePassword,
                     private val postLogin: PostLogin,
                     private val getCacheObject: GetCacheObject,
                     private val putCacheObject: PutCacheObject): BasePresenter<LoginView>() {

    var currentUser: String = ""
    set(value) {
        if (value == field)
            return

        field = value

        if (validateCpf(field))
            view?.maskUserCpf()

        view?.changeLoginButtonState(isLoginButtonEnabled())
    }

    var currentPassword: String = ""
    set(value) {
        field = value
        view?.changeLoginButtonState(isLoginButtonEnabled())
    }

    var cachedUserAccountModel: UserAccountModel? = null
    set(value) {
        field = value
        view?.showWelcomeBack()
    }

    fun handleLoginButtonClick() {
        view?.showLoading()
        try {
            postLogin
                .withParams(currentUser, currentPassword)
                .execute()
                .subscribe({ handlePostLoginSuccess(it) }, { handlePostLoginError(it) })
        }
        catch (e : InvalidLoginException) {
            e.printStackTrace()
            view?.hideLoading()
            view?.showLoginError()
        }
    }

    fun retrieveUserAccount() {
        try {
            val userAccountDomain = getCacheObject.withParams(CacheHelper.UserAccount, UserAccountDomain::class.java).execute() as UserAccountDomain
            cachedUserAccountModel = PresentationModelMapper.mapUserAccount(userAccountDomain)
        }
        catch (e : Exception) {
            e.printStackTrace()
            view?.hideWelcomeBack()
        }
    }

    private fun handlePostLoginError(error: Throwable) {
        error.printStackTrace()
        view?.hideLoading()
        view?.showLoginError()
    }

    private fun handlePostLoginSuccess(response: LoginResponseDomain) {
        val responseModel = PresentationModelMapper.mapLoginResponse(response)
        if (responseModel.error != null && !responseModel.error?.message.isNullOrEmpty()) {
            view?.hideLoading()
            view?.showLoginError(responseModel.error?.message)
            return
        }

        if (responseModel.userAccount == null) {
            view?.hideLoading()
            view?.showLoginError()
            return
        }

        putCacheObject.withParams(CacheHelper.UserAccount, response.userAccount!!).execute()
        navigateToStatements(responseModel.userAccount!!)
        view?.hideLoading()
    }

    private fun isLoginButtonEnabled(): Boolean {
        val isUserValid = validateCpf(currentUser) || validateEmail(currentUser)
        val isPasswordValid = validatePassword(currentPassword)
        return isUserValid && isPasswordValid
    }

    private fun validateCpf(text: String): Boolean {
        return validateCpf.withParams(text).execute()
    }

    private fun validateEmail(text: String): Boolean {
        return validateEmail.withParams(text).execute()
    }

    private fun validatePassword(text: String): Boolean {
        return validatePassword.withParams(text).execute()
    }

    private fun navigateToStatements(userAccountModel: UserAccountModel) {
        navigator.navigateToStatement(userAccountModel)
    }

}
