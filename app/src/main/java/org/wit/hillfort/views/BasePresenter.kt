package org.wit.hillfort.views

import android.content.Intent
import org.wit.hillfort.main.MainApp

open class BasePresenter(var view: BaseView?) {

    // reference to the main app
    var app: MainApp = view?.application as MainApp

    open fun doActivityResult(requestCode: Int, resultCode: Int, data: Intent) {
    }

    open fun doRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
    }

    open fun onDestroy() {
        // set the view to null to avoid potential null pointer violations
        view = null
    }
}