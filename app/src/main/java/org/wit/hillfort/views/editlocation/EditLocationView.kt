package org.wit.hillfort.views.editlocation

import android.os.Bundle
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.Marker
import org.wit.hillfort.R
import org.wit.hillfort.views.BaseView

class EditLocationView : BaseView(), GoogleMap.OnMarkerDragListener, GoogleMap.OnMarkerClickListener {

  lateinit var map: GoogleMap
  lateinit var presenter: EditLocationPresenter

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_edit_location)
    // obtain the SupportMapFragment and get notified when the map is ready to be used
    val mapFragment = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
    presenter = initPresenter(EditLocationPresenter(this)) as EditLocationPresenter
    mapFragment.getMapAsync {
      map = it
      map.setOnMarkerDragListener(this)
      map.setOnMarkerClickListener(this)
      presenter.doConfigureMap(map)
    }
  }

  override fun onMarkerDragStart(marker: Marker) {}

  override fun onMarkerDrag(marker: Marker) {}

  override fun onMarkerDragEnd(marker: Marker) {
    presenter.doUpdateLocation(marker.position.latitude, marker.position.longitude)
  }

  override fun onBackPressed() {
    presenter.doSave()
  }

  override fun onMarkerClick(marker: Marker): Boolean {
    presenter.doUpdateMarker(marker)
    return false
  }
}