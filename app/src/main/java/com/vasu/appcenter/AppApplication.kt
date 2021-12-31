package com.vasu.appcenter

import android.app.Activity
import android.content.Context
import android.util.Log
import androidx.multidex.MultiDex
import com.example.app.adshelper.VasuAdsConfig
import com.example.app.adshelper.openad.AppOpenApplication
import com.example.latest.vasu.newappcenter.MoreAppsActivity
import com.example.latest.vasu.newappcenter.utils.getBoolean

class AppApplication : AppOpenApplication(), AppOpenApplication.AppLifecycleListener {

    private val TAG = javaClass.simpleName

    override fun attachBaseContext(base: Context?) {
        super.attachBaseContext(base)
        MultiDex.install(this)
    }

    override fun onCreate() {
        super.onCreate()

        setAppLifecycleListener(this)

        Log.e(TAG, "onCreate: IS_OPEN_ADS_ENABLE::${this.getBoolean(IS_OPEN_ADS_ENABLE, true)}")

        VasuAdsConfig.with(this)
            .isEnableOpenAd(this.getBoolean(IS_OPEN_ADS_ENABLE, true))
            .initialize()

        initMobileAds(isAppInTesting = true)
    }

    override fun onResumeApp(fCurrentActivity: Activity): Boolean {
        Log.e(TAG, "onResumeApp: fCurrentActivity::${fCurrentActivity.localClassName}")
        val isNeedToShowAd: Boolean = when {
            fCurrentActivity is SplashActivity -> {
                Log.d(TAG, "onResumeApp: fCurrentActivity is SplashActivity")
                false
            }

            fCurrentActivity is MoreAppsActivity -> {
                Log.d(TAG, "onResumeApp: fCurrentActivity is MoreAppsActivity")
                false
            }

            fCurrentActivity is SecondActivity -> {
                Log.d(TAG, "onResumeApp: fCurrentActivity is ExitActivity")
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