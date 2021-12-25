package com.vasu.appcenter

import android.content.Context
import android.util.Log
import androidx.multidex.MultiDex
import androidx.multidex.MultiDexApplication
import com.example.app.adshelper.VasuAdsConfig
import com.example.app.adshelper.setTestDeviceIds
import com.google.android.gms.ads.MobileAds

class AppApplication : MultiDexApplication() {

    private val TAG = javaClass.simpleName

    override fun attachBaseContext(base: Context?) {
        super.attachBaseContext(base)
        MultiDex.install(this)
    }

    override fun onCreate() {
        super.onCreate()

        VasuAdsConfig.with(this)
            .setAdmobInterstitialAdId("ca-app-pub-1168261283036318/6243240347")
            .initialize()

        MobileAds.initialize(this) {
            Log.d(TAG, "onInitializationComplete.")
            setTestDeviceIds()
        }
    }
}