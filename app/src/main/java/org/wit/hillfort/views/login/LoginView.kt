package org.wit.hillfort.views.login

import android.os.Bundle
import android.view.View
import android.widget.ProgressBar
import kotlinx.android.synthetic.main.activity_login.*
import org.jetbrains.anko.toast
import org.wit.hillfort.R
import org.wit.hillfort.views.BaseView

class LoginView : BaseView() {

    lateinit var presenter: LoginPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        init(toolbar, false)
        progressBar.visibility = ProgressBar.INVISIBLE
        presenter = initPresenter(LoginPresenter(this)) as LoginPresenter

        btn_signup.setOnClickListener {
            val email = txt_email.text.toString().trim()
            val password = txt_password.text.toString().trim()
            // validate fields
            if (email.isEmpty() || password.isEmpty()) {
                toast(R.string.toast_enterLoginDetails)
            } else {
                presenter.doSignUp(email,password)
            }
        }

        btn_login.setOnClickListener {
            val email = txt_email.text.toString()
            val password = txt_password.text.toString()
            // validate fields
            if (email.isEmpty() || password.isEmpty()) {
                toast(R.string.toast_enterLoginDetails)
            } else {
                presenter.doLogin(email,password)
            }
        }
    }

    override fun showProgress() {
        progressBar.visibility = View.VISIBLE
    }

    override fun hideProgress() {
        progressBar.visibility = View.GONE
    }
}