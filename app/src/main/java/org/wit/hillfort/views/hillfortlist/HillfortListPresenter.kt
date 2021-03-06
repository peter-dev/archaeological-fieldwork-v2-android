package org.wit.hillfort.views.hillfortlist

import com.google.firebase.auth.FirebaseAuth
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread
import org.wit.hillfort.models.HillfortModel
import org.wit.hillfort.views.BasePresenter
import org.wit.hillfort.views.BaseView
import org.wit.hillfort.views.VIEW

class HillfortListPresenter(view: BaseView) : BasePresenter(view) {

    fun doLogout() {
        FirebaseAuth.getInstance().signOut()
        doAsync {
            app.hillforts.clear()
            uiThread {
                view?.navigateTo(VIEW.LOGIN)
                // dismiss the activity
                view?.finish()
            }
        }
    }

    fun doAddHillfort() {
        view?.navigateTo(VIEW.HILLFORT)

    }

    fun doEditHilfort(hillfort: HillfortModel) {
        view?.navigateTo(VIEW.HILLFORT, 0, "hillfort_edit", hillfort)
    }

    fun doShowHillfortsMap() {
        view?.navigateTo(VIEW.MAPS)
    }

    fun doShowUserSettings() {
        view?.navigateTo(VIEW.SETTINGS)
    }

    fun loadHillforts() {
        doAsync {
            val hillforts = app.hillforts.findAll()
            uiThread {
                view?.showHillforts(hillforts)
            }
        }
    }

    fun loadRingfortsSearch(containingString: String) {
        doAsync {
            val hillforts = app.hillforts.findAll()
            val filtered = hillforts.filter { it.title.toLowerCase().contains(containingString.toLowerCase()) }
            uiThread {
                view?.showHillforts(filtered)
            }
        }
    }
}