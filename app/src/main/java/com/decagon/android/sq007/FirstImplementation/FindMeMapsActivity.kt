package com.decagon.android.sq007.FirstImplementation

import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.location.LocationManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.decagon.android.sq007.R
import com.google.android.gms.location.*

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.firebase.database.*
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class FindMeMapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var findMeMap: GoogleMap
    private lateinit var findMeMapLocationManager: LocationManager
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var locationRequest: LocationRequest
    private lateinit var locationCallback: LocationCallback
    private lateinit var firebaseRef: DatabaseReference
    private val requestCode = 100
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_find_me_maps)
        findMeMapLocationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        val REQUEST_CODE = 100
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
        checkPermission()
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        firebaseRef = Firebase.database.reference
        firebaseRef.addValueEventListener(logListener)
    }
    /*
   this function uses the location callback from the fusedLocationProvider to provide
   location updates and sends those location updates to firebase database
    */

    private fun getLocationUpdates() {
        locationRequest = LocationRequest.create()
        locationRequest.interval = 30000
        locationRequest.fastestInterval = 20000
        locationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY

        locationCallback = object : LocationCallback() {
            override fun onLocationResult(p0: LocationResult) {
                super.onLocationResult(p0)
                if (p0.locations.isNotEmpty()) {
                    val location = p0.lastLocation
                    val firebaseDatabase = FirebaseDatabase.getInstance()
                    val firebaseRef = firebaseDatabase.reference
                    val locationLogging =
                        LocationDetails(
                            location.latitude,
                            location.longitude
                        )
                    firebaseRef.child("UserLocationNode").setValue(locationLogging)
                        .addOnSuccessListener {
                            Toast.makeText(
                                this@FindMeMapsActivity,
                                "Location Added To Database",
                                Toast.LENGTH_LONG
                            ).show()
                        }
                        .addOnFailureListener {
                            Toast.makeText(
                                this@FindMeMapsActivity,
                                "Failed To Add Location Details To Database",
                                Toast.LENGTH_LONG
                            ).show()
                        }
                    if (location != null) {
                        val latLng = LatLng(location.latitude, location.longitude)
                        val markerOptions = MarkerOptions().position(latLng).title("Salawu")
                        findMeMap.addMarker(markerOptions.title("Salawu"))
                        findMeMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 20f))
                        findMeMap.clear()
                    }
                }
            }
        }
    }

    /**
     *This function handles reading of data from the child node that was sent to firebase database
     * if snapshot exists the data from the node is saved in a variable and and the corresponding
     * Latitudes and Longitudes are used to get the location and set the marker
     */


    val logListener = object : ValueEventListener {
        override fun onCancelled(error: DatabaseError) {
            Toast.makeText(
                this@FindMeMapsActivity,
                "Unable to read from database",
                Toast.LENGTH_LONG
            ).show()
        }

        override fun onDataChange(snapshot: DataSnapshot) {
            if (snapshot.exists()) {
                val locationDetails = snapshot.child("user").getValue(LocationDetails::class.java)
                val teamMateLat = locationDetails?.latitude
                val teamMateLong = locationDetails?.longitude

                if (teamMateLat != null && teamMateLong != null) {
                    val teamMateLocation = LatLng(teamMateLat, teamMateLong)
                    val markerOptions = MarkerOptions().position(teamMateLocation).title("Bawo")
                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_MAGENTA))
                    findMeMap.addMarker(markerOptions)
                    findMeMap.animateCamera(CameraUpdateFactory.newLatLngZoom(teamMateLocation, 20f))
                    Toast.makeText(
                        this@FindMeMapsActivity,
                        "Location accessed from the database",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
        }

    }

    private fun startLocationUpdates() {
        if (ActivityCompat.checkSelfPermission(
                this,
                android.Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                android.Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION),
                requestCode
            )
            return
        }
        fusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, null)
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    override fun onMapReady(googleMap: GoogleMap) {
        findMeMap = googleMap
        getLocationPermission()
    }

    fun checkPermission() {
        if (ActivityCompat.checkSelfPermission(
                this,
                android.Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            askForPermission()
        }
    }

    /*
    This function checks if the location permission has been granted
    if it has been the function calls the getLocationUpdates funtion
    and also the startLocationUpdates function. if not it asks for permission
     */
    fun getLocationPermission() {
        if (ContextCompat.checkSelfPermission(
                this,
                android.Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            findMeMap.isMyLocationEnabled = true
            getLocationUpdates()
            startLocationUpdates()
        } else {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION),
                requestCode
            )
        }
    }

    private fun askForPermission() {
        ActivityCompat.requestPermissions(
            this as Activity,
            arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION),
            requestCode
        )
    }

    /**
     *Determines the actions to be taken when permission is requested
     */

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == this.requestCode) {
            if (grantResults.contains(PackageManager.PERMISSION_GRANTED)) {
                getLocationPermission()
            } else {
                Toast.makeText(this, "Permission not granted by user", Toast.LENGTH_LONG).show()
                finish()
            }
        }
    }
}
