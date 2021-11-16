package com.example.latest.vasu.newappcenter.utils

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.graphics.drawable.Drawable
import android.net.Uri
import androidx.core.graphics.drawable.DrawableCompat
import com.example.latest.vasu.newappcenter.R
import com.example.latest.vasu.newappcenter.themeColor
import com.example.app.base.utils.getDrawableRes

var PKG_NAME = "com.latest.status.message.text.jokes.funny.test" // com.latest.status.message.text.jokes.funny

@kotlin.jvm.JvmField
var isMoreAppsClick = false

inline val Context.shapeCategorySelectedDrawable: Drawable?
    get() {
        val unwrappedDrawable: Drawable? =
            this.getDrawableRes(R.drawable.shape_category_selected)
        if (unwrappedDrawable != null) {
            themeColor?.let {
                val wrappedDrawable: Drawable = DrawableCompat.wrap(unwrappedDrawable)
                DrawableCompat.setTint(wrappedDrawable, it)
                return wrappedDrawable
            }
        }

        return unwrappedDrawable
    }

//<editor-fold desc="For Share & Rate App">
fun Context.shareApp(msg: String?) {
    isMoreAppsClick = true
    try {
        val intent = Intent(Intent.ACTION_SEND)
        intent.type = "text/plain"
        intent.putExtra(Intent.EXTRA_TEXT, msg)
        startActivity(Intent.createChooser(intent, "Share Via"))
    } catch (e: java.lang.Exception) {
        android.util.Log.e("shareApp", "shareApp: $e")
    }
}

fun Context.rateApp(pkgName: String?) {
    isMoreAppsClick = true
    try {
        val marketUri = Uri.parse("market://details?id=$pkgName")
        val marketIntent = Intent(Intent.ACTION_VIEW, marketUri)
        startActivity(marketIntent)
    } catch (e: ActivityNotFoundException) {
        val marketUri = Uri.parse("https://play.google.com/store/apps/details?id=$pkgName")
        val marketIntent = Intent(Intent.ACTION_VIEW, marketUri)
        startActivity(marketIntent)
    }
}
//</editor-fold>