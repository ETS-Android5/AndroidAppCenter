@file:Suppress("unused")

package com.example.app.adshelper

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import androidx.annotation.StringRes
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.MobileAds
import com.google.android.gms.ads.RequestConfiguration
import java.io.IOException


var isInterstitialAdShow = false
var isNeedToShowAds = true

internal var admob_app_id: String? = null
internal var admob_banner_ad_id: String? = null
internal var admob_interstitial_ad_id: String? = null
internal var admob_native_advanced_ad_id: String? = null
internal var admob_reward_video_ad_id: String? = null
internal var admob_interstitial_ad_reward_id: String? = null

/**
 * Extension method to Get String resource for Context.
 */
internal fun Context.getStringRes(@StringRes id: Int) = resources.getString(id)

//<editor-fold desc="For View">
/**
 * Extension method to get LayoutInflater
 */
internal inline val Context.inflater: LayoutInflater get() = LayoutInflater.from(this)

/**
 * Show the view  (visibility = View.VISIBLE)
 */
internal inline val View.visible: View
    get() {
        if (visibility != View.VISIBLE) {
            visibility = View.VISIBLE
        }
        return this
    }

/**
 * Hide the view. (visibility = View.INVISIBLE)
 */
internal inline val View.invisible: View
    get() {
        if (visibility != View.INVISIBLE) {
            visibility = View.INVISIBLE
        }
        return this
    }

/**
 * Remove the view (visibility = View.GONE)
 */
internal inline val View.gone: View
    get() {
        if (visibility != View.GONE) {
            visibility = View.GONE
        }
        return this
    }
//</editor-fold>

/**
 * ToDo.. Return true if internet or wi-fi connection is working fine
 * <p>
 * Required permission
 * <uses-permission android:name="android.permission.INTERNET"/>
 *
 * @return true if you have the internet connection, or false if not.
 */
inline val isOnline: Boolean
    get() {
        return try {
            val command = "ping -c 1 google.com"
            return Runtime.getRuntime().exec(command).waitFor() == 0
        } catch (e: IOException) {
            false
        }
    }

/**
 * Extension method for add AdMob Ads test devise id's
 *
 * Find This Log in your logcat for get your devise test id
 * I/Ads: Use RequestConfiguration.Builder.setTestDeviceIds(Arrays.asList("33BE2250B43518CCDA7DE426D04EE231"))
 */
fun setTestDeviceIds() {

    val lTestDeviceIds = listOf(
        AdRequest.DEVICE_ID_EMULATOR
    )
    val lConfiguration = RequestConfiguration.Builder().setTestDeviceIds(lTestDeviceIds).build()

    MobileAds.setRequestConfiguration(lConfiguration)
}

/**
 * Extension method for add different size of Native Ad
 */
enum class NativeAdsSize {
    Big, Medium, FullScreen
}