package org.wit.hillfort.views.hillfort

import android.annotation.SuppressLint
import android.content.Intent
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread
import org.wit.hillfort.helpers.checkLocationPermissions
import org.wit.hillfort.helpers.createDefaultLocationRequest
import org.wit.hillfort.helpers.isPermissionGranted
import org.wit.hillfort.helpers.showImagePicker
import org.wit.hillfort.models.HillfortModel
import org.wit.hillfort.models.Location
import org.wit.hillfort.views.*
import java.time.LocalDate

class HillfortPresenter(view: BaseView) : BasePresenter(view) {

    var map: GoogleMap? = null
    var locationService: FusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(view)
    val locationRequest = createDefaultLocationRequest()
    var defaultLocation = Location(52.245696, -7.139102, 15f)

    var hillfort = HillfortModel()
    var edit = false


    init {
        // retrieve the hillfort, and place its field into the view controls
        if (view.intent.hasExtra("hillfort_edit")) {
            edit = true
            hillfort = view.intent.extras?.getParcelable<HillfortModel>("hillfort_edit")!!
            view.showHillfort(hillfort)
        } else {
            if (checkLocationPermissions(view)) {
                doSetCurrentLocation()
            }
        }
    }

    @SuppressLint("MissingPermission")
    fun doSetCurrentLocation() {
        locationService.lastLocation.addOnSuccessListener {
            locationUpdate(Location(it.latitude, it.longitude))
        }
    }

    @SuppressLint("MissingPermission")
    fun doRestartLocationUpdates() {
        var locationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult?) {
                if (locationResult != null && locationResult.locations != null) {
                    val l = locationResult.locations.last()
                    locationUpdate(Location(l.latitude, l.longitude))
                }
            }
        }
        if (!edit) {
            locationService.requestLocationUpdates(locationRequest, locationCallback, null)
        }
    }

    override fun doRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        if (isPermissionGranted(requestCode, grantResults)) {
            doSetCurrentLocation()
        } else {
            locationUpdate(defaultLocation)
        }
    }

    fun doConfigureMap(m: GoogleMap) {
        map = m
        locationUpdate(hillfort.location)
    }

    // update the location and instruct the view to update the view controls
    fun locationUpdate(location: Location) {
        hillfort.location = location
        hillfort.location.zoom = 15f
        map?.clear()
        map?.uiSettings?.setZoomControlsEnabled(true)
        val options =
            MarkerOptions().title(hillfort.title).position(LatLng(hillfort.location.lat, hillfort.location.lng))
        map?.addMarker(options)
        map?.moveCamera(
            CameraUpdateFactory.newLatLngZoom(
                LatLng(hillfort.location.lat, hillfort.location.lng),
                hillfort.location.zoom
            )
        )
        view?.showHillfort(hillfort)
    }

    // persist hillfort into the data store
    fun doAddOrSave(title: String, description: String) {
        hillfort.title = title
        hillfort.description = description

        doAsync {
            if (edit) {
                app.hillforts.update(hillfort)
            } else {
                app.hillforts.create(hillfort)
            }
            uiThread {
                view?.finish()
            }
        }
    }

    fun doSetDateVisited(isSelected: Boolean) {
        if (isSelected) {
            hillfort.dateVisited = LocalDate.now().toString()
        } else {
            hillfort.dateVisited = "Not Visited"
        }
        hillfort.visited = isSelected
    }

    fun doSetRating(rating: Float) {
        hillfort.rating = rating
    }

    fun doCancel() {
        // finish the activity
        view?.finish()
    }

    fun doDelete() {
        doAsync {
            app.hillforts.delete(hillfort)
            uiThread {
                // finish the activity
                view?.finish()
            }
        }
    }

    fun doSelectImage() {
        view?.let {
            showImagePicker(view!!, IMAGE_REQUEST)
        }
    }

    // redirect to edit location activity
    fun doSetLocation() {
        view?.navigateTo(
            VIEW.LOCATION,
            LOCATION_REQUEST,
            "location",
            Location(hillfort.location.lat, hillfort.location.lng, hillfort.location.zoom)
        )

    }

    override fun doActivityResult(requestCode: Int, resultCode: Int, data: Intent) {
        when (requestCode) {
            IMAGE_REQUEST -> {
                // when a result is returned, recover the image reference
                hillfort.image = data.data.toString()
                view?.showHillfort(hillfort)
            }
            LOCATION_REQUEST -> {
                // when a result is returned, recover the location
                val location = data.extras?.getParcelable<Location>("location")!!
                hillfort.location = location
                locationUpdate(location)
            }
        }
    }
}