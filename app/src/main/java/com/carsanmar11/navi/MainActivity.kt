package com.carsanmar11.navi

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.material.floatingactionbutton.FloatingActionButton

class MainActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var googleMap: GoogleMap
    private lateinit var fused: FusedLocationProviderClient
    //Implementar el follow-mode
    private var followUser = false
    private var locationCallback: com.google.android.gms.location.LocationCallback? = null

    private val requestLocationPerms =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { result ->
            val granted = (result[Manifest.permission.ACCESS_FINE_LOCATION] == true
                    || result[Manifest.permission.ACCESS_COARSE_LOCATION] == true)

            if (granted) {
                enableLocationFeatures()
                centerToLastKnown()
            } else {
                Toast.makeText(this, "Permiso de ubicación denegado", Toast.LENGTH_SHORT).show()
            }
        }

    private fun hasLocationPermission(): Boolean {
        val fine = ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED

        val coarse = ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.ACCESS_COARSE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED

        return fine || coarse
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        fused = LocationServices.getFusedLocationProviderClient(this)

        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.mapFragment) as SupportMapFragment
        mapFragment.getMapAsync(this)

        findViewById<FloatingActionButton>(R.id.fabMyLocation).setOnClickListener {
            if (!hasLocationPermission()) {
                requestLocationPerms.launch(
                    arrayOf(
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.ACCESS_COARSE_LOCATION
                    )
                )
            } else {
                enableLocationFeatures()
                followUser = true
                startLocationUpdates()
                centerToLastKnown() // Clave para que se actualice la posición
            }
        }
        /*
        findViewById<FloatingActionButton>(R.id.fabMyLocation).setOnClickListener {
            if (!hasLocationPermission()) {
                requestLocationPerms.launch(
                    arrayOf(
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.ACCESS_COARSE_LOCATION
                    )
                )
            } else {
                enableLocationFeatures()
                centerToLastKnown()
            }
        }
        */

    }

    override fun onMapReady(map: GoogleMap) {
        googleMap = map

        // Centro inicial para probar (Sevilla)
        val sevilla = LatLng(37.3891, -5.9845)
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(sevilla, 12f))

        // Desactiva botón nativo (usaremos el FAB)
        googleMap.uiSettings.isMyLocationButtonEnabled = false

        // Si ya hay permisos, activa ubicación
        if (hasLocationPermission()) {
            enableLocationFeatures()
        }

        //activar el follow-mode
        googleMap.setOnCameraMoveStartedListener { reason ->
            if (reason == GoogleMap.OnCameraMoveStartedListener.REASON_GESTURE) {
                followUser = false
            }
        }
    }

    // Paramos updates al salir del follow-mode (buenas prácticas)
    override fun onPause() {
        super.onPause()
        locationCallback?.let {
            fused.removeLocationUpdates(it)
        }
    }

    // Con esto reiniciamos updates al volver de segundo plano (para asegurar más todavía)
    override fun onResume() {
        super.onResume()
        if (hasLocationPermission() && ::googleMap.isInitialized && followUser) {
            startLocationUpdates()
        }
    }

    @SuppressLint("MissingPermission")
    private fun enableLocationFeatures() {
        googleMap.isMyLocationEnabled = true
    }

    @SuppressLint("MissingPermission")
    private fun centerToLastKnown() {
        fused.lastLocation.addOnSuccessListener { loc ->
            if (loc != null) {
                val latLng = LatLng(loc.latitude, loc.longitude)
                googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 16f))
            } else {
                Toast.makeText(this, "No se pudo obtener ubicación todavía", Toast.LENGTH_SHORT)
                    .show()
            }
        }
    }

    @SuppressLint("MissingPermission")
    private fun startLocationUpdates() {

        val request = com.google.android.gms.location.LocationRequest.Builder(
            com.google.android.gms.location.Priority.PRIORITY_HIGH_ACCURACY,
            1500L
        ).build()

        locationCallback = object : com.google.android.gms.location.LocationCallback() {
            override fun onLocationResult(result: com.google.android.gms.location.LocationResult) {
                val loc = result.lastLocation ?: return

                if (followUser) {
                    val latLng = LatLng(loc.latitude, loc.longitude)
                    googleMap.animateCamera(
                        CameraUpdateFactory.newLatLngZoom(latLng, 17f)
                    )
                }
            }
        }

        fused.requestLocationUpdates(
            request,
            locationCallback!!,
            mainLooper
        )
    }
}
