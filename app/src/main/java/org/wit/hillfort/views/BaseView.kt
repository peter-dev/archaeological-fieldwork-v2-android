package org.wit.hillfort.views

import android.content.Intent

import android.os.Parcelable
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.google.firebase.auth.FirebaseAuth
import org.jetbrains.anko.AnkoLogger
import org.wit.hillfort.models.HillfortModel
import org.wit.hillfort.models.Location
import org.wit.hillfort.views.editlocation.EditLocationView
import org.wit.hillfort.views.hillfort.HillfortView
import org.wit.hillfort.views.hillfortlist.HillfortListView
import org.wit.hillfort.views.login.LoginView
import org.wit.hillfort.views.map.HillfortMapView
import org.wit.hillfort.views.settings.SettingsView


val IMAGE_REQUEST = 1
val LOCATION_REQUEST = 2

enum class VIEW {
    HILLFORT, LIST, MAPS, LOCATION, LOGIN, SETTINGS
}

abstract class BaseView : AppCompatActivity(), AnkoLogger {

    var basePresenter: BasePresenter? = null

    // utility method for launching activities
    fun navigateTo(view: VIEW, code: Int = 0, key: String = "", value: Parcelable? = null) {
        var intent = Intent(this, HillfortListView::class.java)
        when (view) {
            VIEW.HILLFORT -> intent = Intent(this, HillfortView::class.java)
            VIEW.LIST -> intent = Intent(this, HillfortListView::class.java)
            VIEW.LOCATION -> intent = Intent(this, EditLocationView::class.java)
            VIEW.MAPS -> intent = Intent(this, HillfortMapView::class.java)
            VIEW.LOGIN -> intent = Intent(this, LoginView::class.java)
            VIEW.SETTINGS -> intent = Intent(this, SettingsView::class.java)
        }
        if (key != "") {
            intent.putExtra(key, value)
        }
        startActivityForResult(intent, code)
    }

    // common initialisation and binding of the presenter
    fun initPresenter(presenter: BasePresenter): BasePresenter {
        basePresenter = presenter
        return presenter
    }

    // shared initialisation of the toolbar
    fun init(toolbar: Toolbar, upEnabled: Boolean, showUser: Boolean) {
        toolbar.title = title
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(upEnabled)
        val user = FirebaseAuth.getInstance().currentUser
        if (user != null && showUser) {
            toolbar.title = "ID: ${user.email}"
        }
    }

    // this lifecycle event is called before the activity is destroyed
    override fun onDestroy() {
        basePresenter?.onDestroy()
        super.onDestroy()
    }

    // this lifecycle event is to be triggered when an activity we have started finishes
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (data != null) {
            basePresenter?.doActivityResult(requestCode, resultCode, data)
        }
    }

    // callback for the result from requesting permissions
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        basePresenter?.doRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    open fun showHillfort(hillfort: HillfortModel) {}
    open fun showHillforts(hillforts: List<HillfortModel>) {}
    open fun showLocation(location : Location) {}
    open fun showUser(userEmail: String, totalHillforts: Int, visitedHillforts: Int) {}
    open fun showProgress() {}
    open fun hideProgress() {}
}