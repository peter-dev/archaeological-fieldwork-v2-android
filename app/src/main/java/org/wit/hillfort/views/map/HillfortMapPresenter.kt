package org.wit.hillfort.views.map

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import org.wit.hillfort.models.HillfortModel
import org.wit.hillfort.views.BasePresenter
import org.wit.hillfort.views.BaseView

class HillfortMapPresenter(view: BaseView) : BasePresenter(view) {

    // display a collection of hillfort items on the map
    fun doPopulateMap(map: GoogleMap, hillforts: List<HillfortModel>) {
        map.uiSettings.setZoomControlsEnabled(true)
        hillforts.forEach {
            val loc = LatLng(it.lat, it.lng)
            val options = MarkerOptions()
                .title(it.title)
                .position(loc)
            // keeps reference to hillfort id
            map.addMarker(options).tag = it.id
            map.moveCamera(CameraUpdateFactory.newLatLngZoom(loc, it.zoom))
        }
    }

    // get single hillfort by id and update the view controls
    fun doMarkerSelected(marker: Marker) {
        val tag = marker.tag as Long
        val hillfort = app.hillforts.findById(tag)
        if (hillfort != null) view?.showHillfort(hillfort)

    }

    // fetch all hillforts from the store and update the view
    fun loadHillforts() {
        view?.showHillforts(app.hillforts.findAll())
    }
}