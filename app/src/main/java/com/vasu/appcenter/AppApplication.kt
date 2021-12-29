package com.vasu.appcenter

import android.app.Activity
import android.content.Context
import android.util.Log
import androidx.multidex.MultiDex
import com.example.app.adshelper.VasuAdsConfig
import com.example.app.adshelper.openad.AppOpenApplication
import com.example.latest.vasu.newappcenter.MoreAppsActivity

class AppApplication : AppOpenApplication(), AppOpenApplication.AppLifecycleListener {

    private val TAG = javaClass.simpleName

    override fun attachBaseContext(base: Context?) {
        super.attachBaseContext(base)
        MultiDex.install(this)
    }

    override fun onCreate() {
        super.onCreate()

        setAppLifecycleListener(this)

        VasuAdsConfig.with(this)
            .isEnableOpenAd(true)
            .initialize()

        initMobileAds(isAppInTesting = true)
    }

    override fun onResumeApp(fCurrentActivity: Activity): Boolean {
        val isNeedToShowAd: Boolean = when {
            fCurrentActivity is SplashActivity -> {
                Log.d(TAG, "onResumeApp: fCurrentActivity is SplashActivity")
                false
            }

            fCurrentActivity is MoreAppsActivity -> {
                Log.d(TAG, "onResumeApp: fCurrentActivity is MoreAppsActivity")
                false
            }

            this@AppApplication.isNeedToAdShow -> {
                true
            }
            else -> {
                false
            }
        }

        return isNeedToShowAd
    }
}