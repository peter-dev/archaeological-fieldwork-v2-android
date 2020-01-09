package org.wit.hillfort.views.settings

import android.os.Bundle
import kotlinx.android.synthetic.main.activity_settings.*
import org.jetbrains.anko.AnkoLogger
import org.wit.hillfort.R
import org.wit.hillfort.views.BaseView

class SettingsView : BaseView(), AnkoLogger {

    lateinit var presenter: SettingsPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        init(toolbar, true, false)
        presenter = initPresenter(SettingsPresenter(this)) as SettingsPresenter
        presenter.loadUserStats()
    }

    override fun showUser(userEmail: String, totalHillforts: Int, visitedHillforts: Int) {
        txt_email.setText(userEmail)
        txt_total.setText(totalHillforts.toString())
        txt_visited.setText(visitedHillforts.toString())
    }
}