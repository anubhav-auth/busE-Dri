package com.example.buse_dri

import android.Manifest
import android.app.AlertDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import android.os.Build
import android.os.Bundle
import android.os.Looper
import android.provider.Settings
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.core.app.ActivityCompat
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices

class MainActivity : ComponentActivity() {

    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private var locationRequest: LocationRequest? = null

    @OptIn(ExperimentalPermissionsApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        setContent {
            val permissionState = rememberMultiplePermissionsState(
                permissions = listOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION,
                    Manifest.permission.INTERNET
                )
            )

            NotificationUtils.createNotificationChannel(this)

            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                permissionState.permissions.forEach { perm ->
                    when (perm.permission) {
                        Manifest.permission.ACCESS_FINE_LOCATION -> {
                            when {
                                perm.status.isGranted -> {
                                    // Permission granted, proceed with fetching location
                                }

                                else -> {
                                    // Request permission
                                    ActivityCompat.requestPermissions(
                                        this@MainActivity,
                                        arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                                        1
                                    )
                                }
                            }
                        }

                        Manifest.permission.ACCESS_COARSE_LOCATION -> {
                            when {
                                perm.status.isGranted -> {
                                    // Permission granted, proceed with fetching location
                                }

                                else -> {
                                    // Request permission
                                    ActivityCompat.requestPermissions(
                                        this@MainActivity,
                                        arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION),
                                        1
                                    )
                                }
                            }
                        }

                        Manifest.permission.INTERNET -> {
                            when {
                                perm.status.isGranted -> {
                                    // Permission granted, proceed with fetching location
                                }

                                else -> {
                                    // Request permission
                                    ActivityCompat.requestPermissions(
                                        this@MainActivity,
                                        arrayOf(Manifest.permission.INTERNET),
                                        1
                                    )
                                }
                            }
                        }

                        Manifest.permission.FOREGROUND_SERVICE_LOCATION -> {
                            when {
                                perm.status.isGranted -> {
                                    // Permission granted, proceed with fetching location
                                }

                                else -> {
                                    // Request permission
                                    ActivityCompat.requestPermissions(
                                        this@MainActivity,
                                        arrayOf(Manifest.permission.FOREGROUND_SERVICE_LOCATION),
                                        1
                                    )
                                }
                            }
                        }


                        Manifest.permission.POST_NOTIFICATIONS -> {
                            when {
                                perm.status.isGranted -> {
                                    // Permission granted, proceed with fetching location
                                }

                                else -> {
                                    // Request permission
                                    ActivityCompat.requestPermissions(
                                        this@MainActivity,
                                        arrayOf(Manifest.permission.POST_NOTIFICATIONS),
                                        1
                                    )
                                }
                            }
                        }


                        Manifest.permission.FOREGROUND_SERVICE -> {
                            when {
                                perm.status.isGranted -> {
                                    // Permission granted, proceed with fetching location
                                }

                                else -> {
                                    // Request permission
                                    ActivityCompat.requestPermissions(
                                        this@MainActivity,
                                        arrayOf(Manifest.permission.FOREGROUND_SERVICE),
                                        1
                                    )
                                }
                            }
                        }



                    }
                }


                if (permissionState.allPermissionsGranted) {
                    NotificationUtils.showNotification(this@MainActivity, "hello", "hi")
                    Button(onClick = {

//                        fetchCurrentLocation()
//                        startLocationTrackingService()
                        setupLocationUpdates()

                    }) {
                        Text(text = "Click me")
                    }


                } else {

                    Toast.makeText(
                        this@MainActivity,
                        "Please grant all permissions.",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }





            LaunchedEffect(permissionState) {
                if (permissionState.allPermissionsGranted) {
//                    fetchCurrentLocation()
                }
            }
        }
    }


    private fun fetchCurrentLocation() {

        val locationManager = getSystemService(LOCATION_SERVICE) as LocationManager
        if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            showAlertToEnableGPS()
            return
        } else {
            if (ActivityCompat.checkSelfPermission(
                    this,
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                    this,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ) == PackageManager.PERMISSION_GRANTED
            ) {
                fusedLocationClient.lastLocation.addOnSuccessListener { location: Location? ->
                    location?.let {
//                        Toast.makeText(
//                            this@MainActivity,
//                            "${it.latitude} ${it.longitude}",
//                            Toast.LENGTH_SHORT
//                        ).show()

                        NotificationUtils.updateNotificationContent(this, "Location", "${it.latitude} ${it.longitude}")
                    }
                }
            }
        }


    }

    private fun showAlertToEnableGPS() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("GPS Disabled")
        builder.setMessage("Please enable GPS to continue.")
        builder.setPositiveButton("Open Location Settings") { _, _ ->
            startActivity(Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS))
        }
        builder.setNegativeButton("Cancel", null)
        builder.show()
    }

    private fun setupLocationUpdates() {
        locationRequest = LocationRequest.create().apply {
            interval = 500 // Update location every 1 second
            fastestInterval = 500 // Fastest update interval
            priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        }

        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            fusedLocationClient.requestLocationUpdates(locationRequest!!, locationCallback, Looper.getMainLooper())
        }

    }

    private val locationCallback = object : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult) {
            locationResult.locations.forEach { location ->
                NotificationUtils.updateNotificationContent(this@MainActivity, "Location", "${location.latitude}, ${location.longitude}")
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        fusedLocationClient.removeLocationUpdates(locationCallback)
    }

//    private fun startLocationTrackingService() {
//        val serviceIntent = Intent(this, LocationTrackingService::class.java)
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            startForegroundService(serviceIntent)
//        } else {
//            startService(serviceIntent)
//        }
//    }
}

/*
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ActivityCompat.requestPermissions(
            this,
            arrayOf(
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION,
            ),
            0
        )
        setContent {
            BackgroundLocationTrackingTheme {
                Column(
                    modifier = Modifier.fillMaxSize()
                ) {
                    Button(onClick = {
                        Intent(applicationContext, LocationService::class.java).apply {
                            action = LocationService.ACTION_START
                            startService(this)
                        }
                    }) {
                        Text(text = "Start")
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                    Button(onClick = {
                        Intent(applicationContext, LocationService::class.java).apply {
                            action = LocationService.ACTION_STOP
                            startService(this)
                        }
                    }) {
                        Text(text = "Stop")
                    }
                }
            }
        }
    }
}
 */

