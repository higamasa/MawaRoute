package kies.mawaroute.util

import android.annotation.SuppressLint
import android.app.Activity
import android.content.IntentSender
import android.location.Location
import android.location.LocationManager
import android.os.Looper
import android.widget.Toast
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.LocationSettingsRequest
import com.google.android.gms.location.LocationSettingsStatusCodes
import io.reactivex.subjects.PublishSubject
import kies.mawaroute.R

class GpsUtil(private val activity: Activity) {

    private val fusedLocationClient = LocationServices.getFusedLocationProviderClient(activity)
    val myCurrentLocation: PublishSubject<Location> = PublishSubject.create<Location>()

    companion object {
        val LOCATION_CODE = 100
    }

    fun clean() {
        myCurrentLocation.onComplete()
    }

    /**
     * 位置情報サービスを利用するときの設定.
     */
    private fun createLocationRequest(): LocationRequest {
        return LocationRequest
                .create()
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)  // 高精度
                .setInterval(1000)  // 位置情報更新の間隔(ms)
    }

    /**
     * 位置情報サービスの設定情報を取得するリスナー.
     */
    private fun getLocationSettingResult(): LocationSettingsRequest {
        return LocationSettingsRequest.Builder()
                .addLocationRequest(createLocationRequest())
                .build()
    }

    /**
     * 位置情報をONにしてもらうためのダイアログを表示する
     */
    private fun showLocationDialog(activity: Activity, exception: ResolvableApiException) {
        try {
            exception.startResolutionForResult(activity, LOCATION_CODE)
        } catch (e: IntentSender.SendIntentException) {
            e.printStackTrace()
        }
    }

    private fun locationCallBack(): LocationCallback {
        return object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult) {
                fusedLocationClient.removeLocationUpdates(this)
                myCurrentLocation.onNext(locationResult.lastLocation)
            }
        }
    }

    @SuppressLint("MissingPermission")
    fun accept() {
        val pendingResult = LocationServices.getSettingsClient(activity).checkLocationSettings(getLocationSettingResult())

        pendingResult.addOnCompleteListener { task ->
            try {
                task.getResult(ApiException::class.java)
                // すべての位置情報設定が有効である
                pendingResult
                        .addOnSuccessListener {
                            fusedLocationClient.requestLocationUpdates(
                                    createLocationRequest(), locationCallBack(), Looper.myLooper())
                        }
                        .addOnFailureListener {
                            Toast.makeText(activity, R.string.failed_network_connect, Toast.LENGTH_LONG).show()
                        }
            } catch (exception: ApiException) {
                when (exception.statusCode) {
                    LocationSettingsStatusCodes.RESOLUTION_REQUIRED -> {
                        // 位置情報設定が有効でないためユーザーに修正を求める
                        showLocationDialog(activity, exception as ResolvableApiException)
                    }
                }
            }
        }
    }

    fun reject() {
        val location = Location(LocationManager.GPS_PROVIDER)
        // 東京駅!!
        location.latitude = 35.6811716
        location.longitude = 139.7648629
        myCurrentLocation.onNext(location)
    }
}
