package org.wit.hillfort.views.editlocation

import android.content.Intent
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import org.wit.hillfort.models.Location
import org.wit.hillfort.views.BasePresenter
import org.wit.hillfort.views.BaseView

class EditLocationPresenter(view: BaseView) : BasePresenter(view) {

  var location = Location()

  init {
    location = view.intent.extras?.getParcelable<Location>("location")!!
  }

  // display a hillfort location on the map
  fun doConfigureMap(map: GoogleMap) {
    val loc = LatLng(location.lat, location.lng)
    val options = MarkerOptions()
      .title("Hillfort")
      .snippet("GPS : " + loc.toString())
      .draggable(true)
      .position(loc)
    map.addMarker(options)
    map.moveCamera(CameraUpdateFactory.newLatLngZoom(loc, location.zoom))
  }

  // update location reference
  fun doUpdateLocation(lat: Double, lng: Double) {
    location.lat = lat
    location.lng = lng
  }

  // save current location and finish activity
  fun doSave() {
    val resultIntent = Intent()
    resultIntent.putExtra("location", location)
    view?.setResult(0, resultIntent)
    view?.finish()
  }

  // display a text snippet above a clicked marker
  fun doUpdateMarker(marker: Marker) {
    val loc = LatLng(location.lat, location.lng)
    marker.setSnippet("GPS : " + loc.toString())
  }
}
