package com.vasu.appcenter

import android.graphics.Color
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.example.app.adshelper.*
import com.example.app.adshelper.InterstitialAdHelper.isShowInterstitialAd
import com.example.app.adshelper.InterstitialRewardHelper.isShowRewardedInterstitialAd
import com.example.app.adshelper.InterstitialRewardHelper.showRewardedInterstitialAd
import com.example.app.adshelper.RewardVideoHelper.isShowRewardVideoAd
import com.example.app.adshelper.RewardVideoHelper.showRewardVideoAd
import com.example.app.adshelper.dialogs.FullScreenNativeAdDialog
import com.example.app.base.BaseBindingActivity
import com.example.app.base.utils.getDrawableRes
import com.example.app.base.utils.gone
import com.example.app.base.utils.visible
import com.example.latest.vasu.newappcenter.MoreApps
import com.vasu.appcenter.databinding.ActivityMainBinding


class MainActivity : BaseBindingActivity<ActivityMainBinding>() {


    override fun getActivityContext(): AppCompatActivity {
        return this@MainActivity
    }

    override fun setBinding(): ActivityMainBinding {
        return ActivityMainBinding.inflate(layoutInflater)
    }

    override fun initAds() {
        super.initAds()

        InterstitialAdHelper.loadInterstitialAd(fContext = mActivity)
        RewardVideoHelper.loadRewardVideoAd(fContext = mActivity)
        InterstitialRewardHelper.loadRewardedInterstitialAd(fContext = mActivity)

        mBinding.adsSwitch.setOnCheckedChangeListener { _, isChecked ->
            Log.e(TAG, "initAds: setOnCheckedChangeListener isChecked::$isChecked")
            if (NativeAdvancedModelHelper.getNativeAd != null) {
                NativeAdvancedModelHelper.destroy()
            }

            NativeAdvancedModelHelper(mActivity).loadNativeAdvancedAd(
                NativeAdsSize.Big,
                mBinding.flNativeAdPlaceHolderBig,
                isAddVideoOptions = isChecked,
                isAdLoaded = {
                    NativeAdvancedModelHelper(mActivity).loadNativeAdvancedAd(
                        NativeAdsSize.Medium,
                        mBinding.flNativeAdPlaceHolderMedium,
                        isAddVideoOptions = isChecked,
                    )
                }
            )
        }

        GiftIconHelper.loadGiftAd(
            fContext = mActivity,
            fivGiftIcon = mBinding.layoutHeader.layoutGiftAd.giftAdIcon,
            fivBlastIcon = mBinding.layoutHeader.layoutGiftAd.giftBlastAdIcon
        )
    }

    override fun initView() {
        super.initView()

        mBinding.layoutHeader.ivHeaderBack.setImageDrawable(mActivity.getDrawableRes(R.drawable.ic_new_header_back))
        mBinding.layoutHeader.ivHeaderRightIcon.gone
    }

    override fun initViewAction() {
        super.initViewAction()

        //<editor-fold desc="Reward Video Ad Work">
        mBinding.showRewardVideoAds.alpha = 0.5f
        mBinding.showRewardVideoAds.isEnabled = false

        mActivity.isShowRewardVideoAd(
            onStartToLoadRewardVideoAd = {
                mBinding.showRewardVideoAds.alpha = 0.5f
                mBinding.showRewardVideoAds.isEnabled = false
            },
            onUserEarnedReward = { isUserEarnedReward ->
                Log.e(TAG, "initView: isUserEarnedReward::$isUserEarnedReward")
                mBinding.showRewardVideoAds.alpha = 0.5f
                mBinding.showRewardVideoAds.isEnabled = false
            },
            onAdLoaded = {
                mBinding.showRewardVideoAds.alpha = 1f
                mBinding.showRewardVideoAds.isEnabled = true
            }
        )
        //</editor-fold>

        //<editor-fold desc="Reward Interstitial Video Ad Work">
        mBinding.showRewardInterstitialAds.alpha = 0.5f
        mBinding.showRewardInterstitialAds.isEnabled = false

        mActivity.isShowRewardedInterstitialAd(
            onStartToLoadRewardedInterstitialAd = {
                mBinding.showRewardInterstitialAds.alpha = 0.5f
                mBinding.showRewardInterstitialAds.isEnabled = false
            },
            onUserEarnedReward = { isUserEarnedReward ->
                Log.e(TAG, "initView: isUserEarnedReward::$isUserEarnedReward")
                mBinding.showRewardInterstitialAds.alpha = 0.5f
                mBinding.showRewardInterstitialAds.isEnabled = false
            },
            onAdLoaded = {
                mBinding.showRewardInterstitialAds.alpha = 1f
                mBinding.showRewardInterstitialAds.isEnabled = true
            }
        )
        //</editor-fold>
    }

    override fun initViewListener() {
        super.initViewListener()

        mBinding.adsSwitch.isChecked = true

        setClickListener(
            mBinding.layoutHeader.ivHeaderBack,
            mBinding.showInterstitialAds,
            mBinding.showRewardVideoAds,
            mBinding.showRewardInterstitialAds,
            mBinding.showFullScreenNativeAd,
            mBinding.showMoreAppView,
        )
    }


    override fun onClick(v: View) {
        when (v) {
            mBinding.layoutHeader.ivHeaderBack -> {
                MoreApps.with(mActivity)
                    .setAppPackageName("com.vehicle.rto.vahan.status.information.register")
                    .setTextColor(Color.BLUE)
                    .setThemeColor(Color.RED)
                    .setShareIcon(R.drawable.ic_share_blue)
                    .setBackIcon(R.drawable.ic_new_header_back)
                    .setShareMessage("abc")
                    .launch()
            }
            mBinding.showInterstitialAds -> {
                mActivity.isShowInterstitialAd { isShowFullScreenAd ->
                    Log.e(TAG, "onClick: isShowFullScreenAd::$isShowFullScreenAd")
                }
            }
            mBinding.showRewardVideoAds -> {
                mActivity.showRewardVideoAd()
            }
            mBinding.showRewardInterstitialAds -> {
                mActivity.showRewardedInterstitialAd()
            }
            mBinding.showFullScreenNativeAd -> {
                FullScreenNativeAdDialog(activity = mActivity).showFullScreenNativeAdDialog(mBinding.adsSwitch.isChecked)
            }
            mBinding.showMoreAppView -> {
                mBinding.svAds.gone
                mBinding.moreAppView.visible
            }
        }
    }

    override fun onBackPressed() {
        if (mBinding.moreAppView.visibility == View.VISIBLE) {
            mBinding.svAds.visible
            mBinding.moreAppView.gone
        } else {
            super.onBackPressed()
        }
    }
}