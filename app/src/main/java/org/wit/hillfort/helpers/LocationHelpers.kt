package org.wit.hillfort.helpers

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.pm.PackageManager
import android.util.Log
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.LocationRequest

val REQUEST_PERMISSIONS_REQUEST_CODE = 34

// helper function to check if permission is required (or already has been granted)
fun checkLocationPermissions(activity: Activity): Boolean {
    if (ActivityCompat.checkSelfPermission(
            activity,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
    ) {
        return true
    } else {
        ActivityCompat.requestPermissions(
            activity,
            arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
            REQUEST_PERMISSIONS_REQUEST_CODE
        )
        return false
    }
}

// helper function to process the result of the response to the permissions dialog
fun isPermissionGranted(code: Int, grantResults: IntArray): Boolean {
    var permissionGranted = false
    if (code == REQUEST_PERMISSIONS_REQUEST_CODE) {
        when {
            grantResults.isEmpty() -> Log.i("Location", "User interaction was cancelled.")
            (grantResults[0] == PackageManager.PERMISSION_GRANTED) -> {
                permissionGranted = true
                Log.i("Location", "Permission Granted.")
            }
            else -> Log.i("Location", "Permission Denied.")
        }
    }
    return permissionGranted
}

// helper function to initialise the location request object
@SuppressLint("RestrictedApi")
fun createDefaultLocationRequest(): LocationRequest {
    val locationRequest = LocationRequest().apply {
        interval = 20000
        fastestInterval = 10000
        priority = LocationRequest.PRIORITY_HIGH_ACCURACY
    }
    return locationRequest
}