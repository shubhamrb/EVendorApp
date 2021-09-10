package com.dbcorp.apkaaada.ui.auth.fragments.location.maps

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.location.Location
import android.location.LocationManager
import android.os.Bundle
import android.os.Looper
import android.provider.Settings
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import com.balooouser.com.utils.Utils


import com.dbcorp.vendorapp.database.SqliteDatabase
import com.dbcorp.vendorapp.helper.Util
import com.dbcorp.vendorapp.model.LoginDetails
import com.dbcorp.vendorapp.network.ApiService
import com.dbcorp.vendorapp.network.InternetConnection
import com.dbcorp.vendorapp.network.RestClient

import com.google.android.gms.location.*
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.tasks.Task
import com.google.android.libraries.places.api.Places
import com.google.gson.Gson

import org.json.JSONException
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*

import com.dbcorp.vendorapp.R
import com.dbcorp.vendorapp.database.UserSharedPreference
import com.dbcorp.vendorapp.ui.servicevendor.VendorActivity
import kotlinx.android.synthetic.main.fragment_maps.*


class MapsFragment : Fragment(), OnMapReadyCallback {
    companion object {
        const val TAG = "MapsFragment"
        const val REQUEST_CODE_LOCATION_PERMISSIONS = 1234
    }



    private lateinit var mContext: Context
    private lateinit var userDetails: LoginDetails
    private lateinit var sessionUser: UserSharedPreference
    private lateinit var mMap: GoogleMap
    private var fusedLocationClient: FusedLocationProviderClient? = null


    private var latitude = 0.0
    private var addressType = "1"
    private  var longitude = 0.0

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mContext = context
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        userDetails = SqliteDatabase(activity).login
    }

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        return inflater.inflate(R.layout.fragment_maps, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        fabSet.setOnClickListener {
            mapLocation.setText(tv_address.text.toString())
        }

        fab_location.setOnClickListener {
            requestNewLocationData()
        }

        tvHome.setOnClickListener {
            addressType="1"
            tvHome.setBackgroundResource(R.drawable.red_gredient_bg)
            tvHome.setTextColor(Color.WHITE)
            tvWork.setBackgroundResource(R.drawable.white_gredient_bg)
            tvWork.setTextColor(Color.BLACK)
        }
        tvWork.setOnClickListener {
            addressType="2"
            tvWork.setBackgroundResource(R.drawable.red_gredient_bg)
            tvWork.setTextColor(Color.WHITE)

            tvHome.setBackgroundResource(R.drawable.white_gredient_bg)

            tvHome.setTextColor(Color.BLACK)
        }
        pinMap.setOnClickListener {
            if (edit_house.text.toString().length==0) {
                Util.show(mContext, "Please enter valid house name")
                return@setOnClickListener
            }

            if (editName.text!!.length == 0) {
                Util.show(mContext, "Please enter name")
                return@setOnClickListener
            }

            if (editMobile.text.toString().length == 0) {
                Util.show(mContext, "Please enter mobile")
                return@setOnClickListener
            }
            if (editEmailId.text.toString().length==0) {
                Util.show(mContext, "Please enter email name")
                return@setOnClickListener
            }

            if (edit_street.text.toString().length==0) {
                Util.show(mContext, "Please enter street name")
                return@setOnClickListener
            }

            sessionUser.setAddress(mapLocation.text.toString(),latitude.toString(),longitude.toString());
            val mv = Intent(activity, VendorActivity::class.java)
            startActivity(mv)
            //addAddress()
        }
        initGoogleMap()
    }
    private  fun setAddress(){

    }

    private fun initGoogleMap() {
        if (!Places.isInitialized()) {
            Places.initialize(mContext, getString(R.string.api_key))
        }
        Places.createClient(mContext)

        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(mContext as AppCompatActivity)

        getLastLocation()
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        if (ActivityCompat.checkSelfPermission(
                        mContext, Manifest.permission.ACCESS_FINE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                        mContext, Manifest.permission.ACCESS_COARSE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED) { return }

        mMap.setOnCameraIdleListener {
            val midLatLng = mMap.cameraPosition.target
            latitude = midLatLng.latitude
            longitude = midLatLng.longitude
            val address = Utils.getAddress(mContext, latitude, longitude)



            if (address.isNotEmpty()) {
//                session.setLocation(
//                    hashMapOf(
//                        "address" to address,
//                        "latitude" to latitude.toString(),
//                        "longitude" to longitude.toString()
//                    ))
                progress_bar.visibility = View.GONE
                tv_address.text = address
                fabSet.isEnabled = true
            } else {
                tv_address.text = "Getting address..."
                progress_bar.visibility = View.VISIBLE
                fabSet.isExtended = false
            }
        }
    }

//    private fun addAddress() {
//        if (InternetConnection.checkConnection(mContext)) {
//            val params: MutableMap<String, String> = HashMap()
//            params["name"] = editName.text.toString()
//            params["number"] = editMobile.text.toString()
//            params["email"] = editEmailId.text.toString()
//            params["pincode"] = "464993"
//            params["landmark"] = edit_house.text.toString()
//            params["address_line2"] = edit_street.text.toString()
//            params["lat"] = latitude.toString()
//            params["lng"] = longitude.toString()
//            params["address_type"] = addressType
//            params["address"] = mapLocation.text.toString()
//            params["user_id"] = userDetails.userId
//            Log.e("params", params.toString())
//            RestClient.post().setAddress(userDetails.sk, ApiService.APP_DEVICE_ID, params).enqueue(object : Callback<String?> {
//                override fun onResponse(call: Call<String?>, response: Response<String?>) {
//                    val gson = Gson()
//                    var `object`: JSONObject? = null
//                    try {
//                        `object` = JSONObject(response.body())
//                        Log.e("message", `object`.getString("message"))
//                        if (`object`.getBoolean("status")) {
//                            Util.show(mContext, "We have booked your service")
//                            val mv = Intent(mContext, InvoiceActivityOrder::class.java)
//                            startActivity(mv)
//                            activity!!.finish();
//
//
//                        } else {
//                            Util.show(mContext, "something is wrong")
//                        }
//                    } catch (e: JSONException) {
//                        e.printStackTrace()
//                    }
//                }
//
//                override fun onFailure(call: Call<String?>, t: Throwable) {
//                    try {
//                        t.printStackTrace()
//                    } catch (e: Exception) {
//                        e.printStackTrace()
//                    }
//                }
//            })
//        }
//    }

    private fun getLastLocation() {
        if (checkPermissions()) {
            if (isLocationEnabled()) {
                if (ActivityCompat.checkSelfPermission(
                                mContext as AppCompatActivity,
                                Manifest.permission.ACCESS_FINE_LOCATION
                        ) != PackageManager.PERMISSION_GRANTED
                        && ActivityCompat.checkSelfPermission(
                                mContext as AppCompatActivity,
                                Manifest.permission.ACCESS_COARSE_LOCATION
                        ) != PackageManager.PERMISSION_GRANTED
                ) {
                    return
                }
                fusedLocationClient!!.lastLocation.addOnCompleteListener { task: Task<Location?> ->
                    val location = task.result
                    if (location == null) {
                        mMap.isMyLocationEnabled = false
                        mMap.uiSettings.isMyLocationButtonEnabled = false

                        requestNewLocationData()
                    } else {
                        val latitude = location.latitude
                        val longitude = location.longitude
                        val address = Utils.getAddress(mContext, latitude, longitude)

//                        userDetails.setLocation(
//                            hashMapOf(
//                                "address" to address,
//                                "latitude" to latitude.toString(),
//                                "longitude" to longitude.toString()
//                            ))

                        val latLng = LatLng(latitude, longitude)

                        val cameraPosition: CameraPosition = CameraPosition.Builder()
                                .target(latLng) // Center Set
                                .zoom(15.0f) // Zoom
                                .bearing(90F) // Orientation of the camera to east
                                .tilt(30F) // Tilt of the camera to 30 degrees
                                .build() // Creates a CameraPosition from the builder
                        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition))
                    }
                }
            } else {
                Toast.makeText(mContext, "Turn on location", Toast.LENGTH_LONG).show()
                val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                startActivity(intent)
            }
        } else {
            requestPermissions()
        }
    }

    private fun requestNewLocationData() {
        val mLocationRequest = LocationRequest()
        mLocationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        mLocationRequest.interval = 0
        mLocationRequest.fastestInterval = 0
        mLocationRequest.numUpdates = 1

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(mContext)
        if (ActivityCompat.checkSelfPermission(
                        mContext, Manifest.permission.ACCESS_FINE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(
                        mContext, Manifest.permission.ACCESS_COARSE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED
        ) { return }

        fusedLocationClient!!.requestLocationUpdates(
                mLocationRequest, mLocationCallback,
                Looper.getMainLooper()
        )
    }

    private val mLocationCallback: LocationCallback = object : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult) {
            val mLastLocation: Location = locationResult.lastLocation
            val latitude = mLastLocation.latitude
            val longitude = mLastLocation.longitude

            val latLng = LatLng(latitude, longitude)
            val cameraPosition: CameraPosition = CameraPosition.Builder()
                    .target(latLng) // Center Set
                    .zoom(15.0f) // Zoom
                    .bearing(90F) // Orientation of the camera to east
                    .tilt(30F) // Tilt of the camera to 30 degrees
                    .build() // Creates a CameraPosition from the builder
            mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition))
        }
    }

    private fun checkPermissions(): Boolean {
        return ActivityCompat.checkSelfPermission(
                mContext,
                Manifest.permission.ACCESS_COARSE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(
                        mContext,
                        Manifest.permission.ACCESS_FINE_LOCATION
                ) == PackageManager.PERMISSION_GRANTED
    }

    private fun requestPermissions() {
        ActivityCompat.requestPermissions(
                mContext as AppCompatActivity, arrayOf(
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION
        ),
                REQUEST_CODE_LOCATION_PERMISSIONS
        )
    }

    private fun isLocationEnabled(): Boolean {
        val locationManager: LocationManager =
                mContext.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
                || locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
    }

    override fun onRequestPermissionsResult(
            requestCode: Int,
            permissions: Array<String?>,
            grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_CODE_LOCATION_PERMISSIONS) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getLastLocation()
            }
        }
    }
}