package com.example.buse_dri

import android.Manifest
import android.app.PendingIntent
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.FirebaseApp
import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.messaging.*


class MainActivity : ComponentActivity() {

    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private var locationRequest: LocationRequest? = null
    private val TAG = "MyLogTag"
    private val TAG2 = "MyLogTag2"
    val messagePayload = hashMapOf(
        "title" to "ALERT!!!",
        "body" to "DRIVER HAS STARTED THE JOURNEY."
    )

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        FirebaseApp.initializeApp(this)


        NotificationUtils.createNotificationChannel(this)

        var allPermissionsGranted by mutableStateOf(false)

        val requestPermissionLauncher =
            registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
                allPermissionsGranted = permissions.values.all { it }

            }


        requestPermissionLauncher.launch(
            arrayOf(
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.INTERNET,
                Manifest.permission.POST_NOTIFICATIONS,
            )

        )


        setContent {
            if (allPermissionsGranted) {
                NotificationUtils.showNotification(this@MainActivity, "hello", "hi")
                Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Button(onClick = {
//                        setupLocationUpdates()
                        Log.d(TAG, "Button clicked")

                        val intent2 = Intent(this@MainActivity, LocationService::class.java)
                        startService(intent2)
                    }) {
                        Text(text = "Start Tracking")
                    }
                }
            } else {

                Toast.makeText(
                    this@MainActivity,
                    "Please grant all permissions.",
                    Toast.LENGTH_LONG
                ).show()


            }
        }


    }

    override fun onPause() {
        super.onPause()
        Log.d(TAG, "onPause")
    }

    override fun onResume() {
        super.onResume()
        Log.d(TAG, "onResume")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d(TAG, "onDestroy")
        stopService(Intent(this, LocationService::class.java))
    }
}