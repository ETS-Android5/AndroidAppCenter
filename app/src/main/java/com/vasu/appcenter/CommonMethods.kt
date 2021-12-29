package com.vasu.appcenter

import android.content.Context
import com.example.app.adshelper.isNeedToShowAds
import com.example.latest.vasu.newappcenter.utils.contain
import com.example.latest.vasu.newappcenter.utils.getBoolean

private const val TAG = "CommonMethods"

const val IS_ADS_REMOVED: String = "is_ads_removed"

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