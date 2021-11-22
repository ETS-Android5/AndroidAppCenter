@file:Suppress("unused")

package com.example.app.adshelper

import android.annotation.SuppressLint
import android.content.Context
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import android.widget.*
import androidx.annotation.NonNull
import androidx.constraintlayout.widget.ConstraintLayout
import com.example.app.adshelper.widgets.BlurDrawable
import com.google.android.gms.ads.*
import com.google.android.gms.ads.nativead.NativeAd
import com.google.android.gms.ads.nativead.NativeAdOptions
import com.google.android.gms.ads.nativead.NativeAdView
import java.lang.StringBuilder

/**
 * @author Akshay Harsoda
 * @since 05 Aug 2021
 *
 * NativeAdvancedHelper.kt - Simple object which has load and handle your multiple size Native Advanced AD data
 */
object NativeAdvancedHelper {

    private var mNativeAd: NativeAd? = null
    private val TAG = "Admob_${javaClass.simpleName}"

    val getNativeAd: NativeAd?
        get() {
            return mNativeAd
        }

    init {
        Log.e(TAG, "NativeAdvancedHelper: init class")
    }

    /**
     * Call this method when you need to load your Native Advanced AD
     * you need to call this method only once in any activity or fragment
     *
     * this method will load your Native Advanced AD with 4 different size like [NativeAdsSize.Small], [NativeAdsSize.Medium], [NativeAdsSize.Big], [NativeAdsSize.ExitDialog], [NativeAdsSize.FullScreen]
     * for Native Advanced AD Size @see [NativeAdsSize] once
     *
     * @param fContext this is a reference to your activity or fragment context
     * @param fSize it indicate your Ad Size
     * @param fLayout FrameLayout for add NativeAd View
     * @param isNeedLayoutShow [by Default value = true] pass false if you do not need to show AD at a time when it's loaded successfully
     * @param isAdLoaded lambda function call when ad isLoaded
     * @param onClickAdClose lambda function call when user click close button of ad
     */
    fun loadNativeAdvancedAd(
        @NonNull fContext: Context,
        @NonNull fSize: NativeAdsSize,
        @NonNull fLayout: FrameLayout,
        isNeedLayoutShow: Boolean = true,
        isAddVideoOptions: Boolean = false,
        isAdLoaded: (isNeedToRemoveCloseButton: Boolean) -> Unit = {},
        onClickAdClose: () -> Unit = {}
    ) {
        Log.i(TAG, "loadAd: ")

        if (mNativeAd == null) {

            val builder =
                AdLoader.Builder(fContext, fContext.getStringRes(R.string.admob_nativead_id))

            builder.forNativeAd { unifiedNativeAd ->
                Log.i(TAG, "loadAd: new live Ad -> ${unifiedNativeAd.headline}")
                loadAdWithPerfectLayout(
                    fContext = fContext,
                    fSize = fSize,
                    fLayout = fLayout,
                    nativeAd = unifiedNativeAd,
                    isNeedLayoutShow = isNeedLayoutShow,
                    isAdLoaded = isAdLoaded,
                    onClickAdClose = onClickAdClose
                )
            }

            if (isAddVideoOptions) {
                val videoOptions = VideoOptions.Builder()
                    .setStartMuted(false)
                    .build()

                val adOptions: NativeAdOptions = NativeAdOptions.Builder()
                    .setVideoOptions(videoOptions)
                    .setMediaAspectRatio(NativeAdOptions.NATIVE_MEDIA_ASPECT_RATIO_SQUARE)
                    .build()

                builder.withNativeAdOptions(adOptions)
            }

            val adLoader = builder.withAdListener(object : AdListener() {

                override fun onAdClosed() {
                    super.onAdClosed()
                    Log.e(TAG, "onAdClosed: ")
                    fLayout.removeAllViews()
                    loadNativeAdvancedAd(
                        fContext = fContext,
                        fSize = fSize,
                        fLayout = fLayout,
                        isNeedLayoutShow = isNeedLayoutShow,
                        isAddVideoOptions = isAddVideoOptions,
                        isAdLoaded = isAdLoaded,
                        onClickAdClose = onClickAdClose
                    )
                }

                override fun onAdOpened() {
                    super.onAdOpened()
                    Log.e(TAG, "onAdOpened: ")
                    mNativeAd = null
                }
            }).build()

            adLoader.loadAd(AdRequest.Builder().build())
        } else {
            Log.i(TAG, "loadAd: old stored Ad")
            loadAdWithPerfectLayout(
                fContext = fContext,
                fSize = fSize,
                fLayout = fLayout,
                nativeAd = mNativeAd!!,
                isNeedLayoutShow = isNeedLayoutShow,
                isAdLoaded = isAdLoaded,
                onClickAdClose = onClickAdClose
            )
        }
    }

    @SuppressLint("InflateParams")
    private fun loadAdWithPerfectLayout(
        @NonNull fContext: Context,
        @NonNull fSize: NativeAdsSize,
        @NonNull fLayout: FrameLayout,
        @NonNull nativeAd: NativeAd,
        isNeedLayoutShow: Boolean = true,
        isAdLoaded: (isNeedToRemoveCloseButton: Boolean) -> Unit = {},
        onClickAdClose: () -> Unit
    ) {

        val adView = when (fSize) {

            NativeAdsSize.Big -> {
                fContext.inflater.inflate(
                    R.layout.layout_google_native_ad_big,
                    null
                ) as NativeAdView
            }

            NativeAdsSize.Medium -> {
                fContext.inflater.inflate(
                    R.layout.layout_google_native_ad_medium,
                    null
                ) as NativeAdView
            }

            NativeAdsSize.Small -> {
                fContext.inflater.inflate(
                    R.layout.layout_google_native_ad_banner,
                    null
                ) as NativeAdView
            }
            NativeAdsSize.ExitDialog -> {
                fContext.inflater.inflate(
                    R.layout.layout_google_native_ad_exit_dialog,
                    null
                ) as NativeAdView
            }
            NativeAdsSize.FullScreen -> {
                if (nativeAd.starRating != null && nativeAd.price != null && nativeAd.store != null) {
                    fContext.inflater.inflate(
                        R.layout.layout_google_native_ad_exit_full_screen_app_store,
                        null
                    ) as ConstraintLayout
                } else {
                    fContext.inflater.inflate(
                        R.layout.layout_google_native_ad_exit_full_screen_website,
                        null
                    ) as NativeAdView
                }
            }
        }


        when (fSize) {
            NativeAdsSize.FullScreen -> {
                populateBlurImageDialogNativeAdView(
                    nativeAd,
                    adView.findViewById(R.id.native_ad_view),
                    onClickAdClose
                )
            }
            NativeAdsSize.ExitDialog -> {
                populateExitDialogNativeAdView(nativeAd, adView.findViewById(R.id.native_ad_view))
            }
            else -> {
                populateNativeAdView(nativeAd, adView as NativeAdView)
            }
        }

        fLayout.removeAllViews()
        fLayout.addView(adView)
        if (isNeedLayoutShow) {
            fLayout.visibility = View.VISIBLE
            if (fSize == NativeAdsSize.FullScreen && nativeAd.starRating != null && nativeAd.price != null && nativeAd.store != null) {
                isAdLoaded.invoke(true)
            } else {
                isAdLoaded.invoke(false)
            }
        } else {
            fLayout.visibility = View.GONE
        }
    }

    private fun populateBlurImageDialogNativeAdView(
        nativeAd: NativeAd,
        adView: NativeAdView,
        onClickAdClose: () -> Unit
    ) {

        mNativeAd = nativeAd

        adView.headlineView = adView.findViewById(R.id.ad_headline)
        adView.mediaView = adView.findViewById(R.id.ad_media)
        adView.imageView = adView.findViewById(R.id.iv_bg_main_image)
        adView.bodyView = adView.findViewById(R.id.ad_body)
        adView.iconView = adView.findViewById(R.id.ad_app_icon)
        adView.starRatingView = adView.findViewById(R.id.ad_stars)
        adView.storeView = adView.findViewById(R.id.ad_store)
        adView.priceView = adView.findViewById(R.id.ad_price)
        adView.advertiserView = adView.findViewById(R.id.ad_advertiser)

        adView.callToActionView = adView.findViewById(R.id.ad_call_to_action)

        (adView.headlineView as TextView).text = nativeAd.headline

        if (nativeAd.mediaContent != null && adView.mediaView != null) {
            nativeAd.mediaContent?.let { mediaContent ->
                adView.mediaView?.setMediaContent(mediaContent)
            }
        } else {
            populateBlurImageDialogNativeAdView(mNativeAd!!, adView, onClickAdClose)
        }


        if (nativeAd.images.size > 0) {
            if (nativeAd.images[0] != null && adView.imageView != null) {
                (adView.imageView as ImageView).setImageDrawable(nativeAd.images[0].drawable!!)

                val blurView: View = adView.findViewById(R.id.blur_view)
                val blurDrawable = BlurDrawable(adView.imageView, 15)
                blurView.background = blurDrawable
            }
        }

        if (nativeAd.body == null && adView.bodyView != null) {
            adView.bodyView?.visibility = View.GONE
        } else if (adView.bodyView != null) {
            adView.bodyView?.visibility = View.VISIBLE
            (adView.bodyView as TextView).text = nativeAd.body
        }

        if (nativeAd.icon == null && adView.iconView != null) {
            adView.iconView?.visibility = View.GONE
        } else if (adView.iconView != null) {
            (adView.iconView as ImageView).setImageDrawable(
                nativeAd.icon?.drawable
            )
            adView.iconView?.visibility = View.VISIBLE
        }

        if (nativeAd.starRating == null && adView.starRatingView != null) {
            adView.starRatingView?.visibility = View.GONE
            (adView.findViewById(R.id.txt_rating) as TextView?)?.visibility = View.GONE
        } else if (adView.starRatingView != null) {
            (adView.findViewById(R.id.txt_rating) as TextView?)?.text =
                nativeAd.starRating!!.toFloat().toString()
            (adView.starRatingView as RatingBar).rating = nativeAd.starRating!!.toFloat()
            adView.starRatingView?.visibility = View.VISIBLE
            (adView.findViewById(R.id.txt_rating) as TextView?)?.visibility = View.VISIBLE
        }

        if (nativeAd.callToAction == null && adView.callToActionView != null) {
            adView.callToActionView?.visibility = View.GONE
        } else if (adView.callToActionView != null) {
            adView.callToActionView?.visibility = View.VISIBLE
            (adView.callToActionView as Button).isSelected = true
            nativeAd.callToAction?.let {
                (adView.callToActionView as Button).text = getCamelCaseString(it)
            }
        }

        if (nativeAd.store == null && adView.storeView != null) {
            adView.storeView?.visibility = View.GONE
        } else if (adView.storeView != null) {
            adView.storeView?.visibility = View.VISIBLE
            nativeAd.store?.let {
                (adView.storeView as TextView).text = it
                if (it.equals("Google Play", false)) {
                    (adView.findViewById(R.id.iv_play_logo) as View?)?.visibility = View.VISIBLE
                } else {
                    (adView.findViewById(R.id.iv_play_logo) as View?)?.visibility = View.GONE
                }
            }
        }

        if (nativeAd.price == null && adView.priceView != null) {
            adView.priceView?.visibility = View.GONE
        } else if (adView.priceView != null) {
            adView.priceView?.visibility = View.VISIBLE
            (adView.priceView as TextView).text = nativeAd.price
        }

        if (nativeAd.advertiser == null && adView.advertiserView != null) {
            adView.advertiserView?.visibility = View.GONE
        } else if (adView.advertiserView != null) {
            adView.advertiserView?.visibility = View.VISIBLE
            (adView.advertiserView as TextView).text = nativeAd.advertiser
        }

        if (adView.storeView?.visibility == View.GONE && adView.priceView?.visibility == View.GONE) {
            (adView.findViewById(R.id.cl_ad_price_store) as View?)?.visibility = View.GONE
        }

        (adView.findViewById(R.id.ad_call_to_close) as Button?)?.let {
            it.setOnClickListener {
                onClickAdClose.invoke()
            }
        }

        adView.setNativeAd(nativeAd)
    }

    private fun populateExitDialogNativeAdView(nativeAd: NativeAd, adView: NativeAdView) {
        Log.i(TAG, Throwable().stackTrace[0].methodName)

        mNativeAd = nativeAd

        adView.headlineView = adView.findViewById(R.id.ad_headline)
        adView.mediaView = adView.findViewById(R.id.ad_media)

        adView.iconView = adView.findViewById(R.id.ad_app_icon)
        adView.bodyView = adView.findViewById(R.id.ad_body)
        adView.callToActionView = adView.findViewById(R.id.ad_call_to_action)


        (adView.headlineView as TextView).text = nativeAd.headline

        if (nativeAd.mediaContent != null && adView.mediaView != null) {
            adView.mediaView!!.setMediaContent(nativeAd.mediaContent!!)
        } else if (adView.mediaView != null) {
            populateExitDialogNativeAdView(nativeAd, adView)
        }

        adView.mediaView?.setImageScaleType(ImageView.ScaleType.CENTER_CROP)

        if (nativeAd.body == null && adView.bodyView != null) {
            adView.bodyView!!.visibility = View.GONE
        } else if (adView.bodyView != null) {
            adView.bodyView!!.visibility = View.VISIBLE
            (adView.bodyView as TextView).text = nativeAd.body
        }

        if (nativeAd.icon == null && adView.iconView != null) {
            adView.iconView!!.visibility = View.GONE
        } else if (adView.iconView != null) {
            (adView.iconView as ImageView).setImageDrawable(
                nativeAd.icon!!.drawable
            )
            adView.iconView!!.visibility = View.VISIBLE
        }

        if (nativeAd.callToAction == null && adView.callToActionView != null) {
            adView.callToActionView!!.visibility = View.GONE
        } else if (adView.callToActionView != null) {
            adView.callToActionView!!.visibility = View.VISIBLE
            (adView.callToActionView as Button).text = nativeAd.callToAction
        }

        adView.setNativeAd(nativeAd)
    }

    private fun populateNativeAdView(nativeAd: NativeAd, adView: NativeAdView) {
        Log.i(TAG, Throwable().stackTrace[0].methodName)

        mNativeAd = nativeAd

        adView.mediaView = adView.findViewById(R.id.ad_media)

        adView.headlineView = adView.findViewById(R.id.ad_headline)
        adView.bodyView = adView.findViewById(R.id.ad_body)
        adView.callToActionView = adView.findViewById(R.id.ad_call_to_action)
        adView.iconView = adView.findViewById(R.id.ad_app_icon)
        adView.priceView = adView.findViewById(R.id.ad_price)
        adView.starRatingView = adView.findViewById(R.id.ad_stars)
        adView.storeView = adView.findViewById(R.id.ad_store)
        adView.advertiserView = adView.findViewById(R.id.ad_advertiser)

        (adView.headlineView as TextView).text = nativeAd.headline

        if (nativeAd.mediaContent != null && adView.mediaView != null) {
            adView.mediaView!!.setMediaContent(nativeAd.mediaContent!!)
        } else {
            populateNativeAdView(mNativeAd!!, adView)
        }

        if (nativeAd.body == null && adView.bodyView != null) {
            adView.bodyView!!.visibility = View.GONE
        } else if (adView.bodyView != null) {
            adView.bodyView!!.visibility = View.VISIBLE
            (adView.bodyView as TextView).text = nativeAd.body
        }

        if (nativeAd.callToAction == null && adView.callToActionView != null) {
            adView.callToActionView!!.visibility = View.INVISIBLE
        } else if (adView.callToActionView != null) {
            adView.callToActionView!!.visibility = View.VISIBLE
            (adView.callToActionView as Button).text = nativeAd.callToAction
        }

        if (nativeAd.icon != null && adView.iconView != null) {
            (adView.iconView as ImageView).setImageDrawable(
                nativeAd.icon!!.drawable
            )
            adView.iconView!!.visibility = View.VISIBLE
        } else if (adView.iconView != null) {
            if (nativeAd.images.size > 0) {
                if (nativeAd.images[0] != null && nativeAd.images[0].drawable != null) {
                    (adView.iconView as ImageView).setImageDrawable(nativeAd.images[0].drawable!!)
                    adView.iconView!!.visibility = View.VISIBLE
                } else {
                    adView.iconView!!.visibility = View.GONE
                }
            } else {
                adView.iconView!!.visibility = View.GONE
            }
        } else {
            adView.iconView!!.visibility = View.GONE
        }

        /*if (nativeAd.icon == null && adView.iconView != null) {
            adView.iconView!!.visibility = View.GONE
        } else if (adView.iconView != null) {
            (adView.iconView as ImageView).setImageDrawable(
                nativeAd.icon!!.drawable
            )
            adView.iconView!!.visibility = View.VISIBLE
        }*/

        if (nativeAd.price == null && adView.priceView != null) {
            adView.priceView!!.visibility = View.INVISIBLE
        } else if (adView.priceView != null) {
            adView.priceView!!.visibility = View.VISIBLE
            (adView.priceView as TextView).text = nativeAd.price
        }

        if (nativeAd.store == null && adView.storeView != null) {
            adView.storeView!!.visibility = View.INVISIBLE
        } else if (adView.storeView != null) {
            adView.storeView!!.visibility = View.VISIBLE
            (adView.storeView as TextView).text = nativeAd.store
        }

        if (adView.priceView != null) {
            adView.priceView!!.visibility = View.GONE
        }
        if (adView.storeView != null) {
            adView.storeView!!.visibility = View.GONE
        }

        if (nativeAd.starRating == null && adView.starRatingView != null) {
            adView.starRatingView!!.visibility = View.GONE
        } else if (adView.starRatingView != null) {
            (adView.starRatingView as RatingBar).rating = nativeAd.starRating!!.toFloat()
            adView.starRatingView!!.visibility = View.VISIBLE
        }

        if (nativeAd.advertiser == null && adView.advertiserView != null) {
            adView.advertiserView!!.visibility = View.GONE
        } else if (adView.advertiserView != null) {
            (adView.advertiserView as TextView).text = nativeAd.advertiser
            adView.advertiserView!!.visibility = View.VISIBLE
        }

        adView.setNativeAd(nativeAd)
    }

    fun destroy() {
        mNativeAd?.destroy()
        mNativeAd = null
    }

    private fun getCamelCaseString(text: String): String {

        val words: Array<String> = text.split(" ").toTypedArray()

        val builder = StringBuilder()
        for (i in words.indices) {
            var word: String = words[i]
            word = if (word.isEmpty()) word else Character.toUpperCase(word[0])
                .toString() + word.substring(1).lowercase()
            builder.append(word)
            if (i != (words.size - 1)) {
                builder.append(" ")
            }
        }
        return builder.toString()
    }
}