package com.ibm.challenge.presentation.view.splashscreen

import android.os.Handler
import com.ibm.challenge.core.Navigator
import com.ibm.challenge.core.mvp.BasePresenter

class SplashScreenPresenter(private val navigator: Navigator): BasePresenter<SplashScreenView>() {

    fun navigateToLogin() {
        val handle = Handler()
        handle.postDelayed({
            navigator.navigateToLogin()
        }, 2000)
    }

}
