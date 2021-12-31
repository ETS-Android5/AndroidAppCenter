package com.vasu.appcenter

import android.content.Context
import com.example.app.adshelper.isNeedToShowAds
import com.example.latest.vasu.newappcenter.utils.contain
import com.example.latest.vasu.newappcenter.utils.getBoolean
import android.content.Intent

import android.content.ComponentName

import android.content.pm.PackageManager




private const val TAG = "CommonMethods"

const val IS_ADS_REMOVED: String = "is_ads_removed"
const val IS_OPEN_ADS_ENABLE: String = "is_open_ads_enable"

/**
 * Extension method to check is need to ad show or not
 */
inline val Context.isNeedToAdShow: Boolean
    get() {
        isNeedToShowAds = if (!this.contain(IS_ADS_REMOVED)) {
            true
        } else {
            !this.getBoolean(IS_ADS_REMOVED)
        }
        return isNeedToShowAds
    }

fun triggerRebirth(context: Context) {
    val packageManager = context.packageManager
    val intent = packageManager.getLaunchIntentForPackage(context.packageName)
    val componentName = intent!!.component
    val mainIntent = Intent.makeRestartActivityTask(componentName)
    context.startActivity(mainIntent)
    Runtime.getRuntime().exit(0)
}