package com.vasu.appcenter

import android.util.Log
import androidx.multidex.MultiDexApplication
import com.example.app.adshelper.setTestDeviceIds
import com.google.android.gms.ads.MobileAds

class AppApplication :  MultiDexApplication() {

    private val TAG = javaClass.simpleName

    override fun onCreate() {
        super.onCreate()

        MobileAds.initialize(this) {
            Log.d(TAG, "onInitializationComplete.")
            setTestDeviceIds()
        }
    }
}