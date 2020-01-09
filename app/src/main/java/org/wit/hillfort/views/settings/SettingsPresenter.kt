package org.wit.hillfort.views.settings

import com.google.firebase.auth.FirebaseAuth
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread
import org.wit.hillfort.views.BasePresenter
import org.wit.hillfort.views.BaseView

class SettingsPresenter(view: BaseView) : BasePresenter(view) {

    var email: String? = ""
    var totalHillforts = 0
    var visitedHillforts = 0


    fun loadUserStats() {
        doAsync {
            val user = FirebaseAuth.getInstance().currentUser
            user?.let {
                 email = user.email
            }
            val hillforts = app.hillforts.findAll()
            val visited = hillforts.filter { it.visited }
            totalHillforts = hillforts.size
            visitedHillforts = visited.size
            uiThread {
                view?.showUser(email!!, totalHillforts, visitedHillforts)
            }
        }
    }

}