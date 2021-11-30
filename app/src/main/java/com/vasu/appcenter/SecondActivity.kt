package com.vasu.appcenter

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.app.adshelper.NativeAdsSize
import com.example.app.adshelper.NativeAdvancedModelHelper
import com.example.app.base.BaseBindingActivity
import com.vasu.appcenter.databinding.ActivitySecondBinding

class SecondActivity : BaseBindingActivity<ActivitySecondBinding>() {

    override fun getActivityContext(): AppCompatActivity {
        return this@SecondActivity
    }

    override fun setBinding(): ActivitySecondBinding {
        return ActivitySecondBinding.inflate(layoutInflater)
    }

    override fun initAds() {
        super.initAds()

        NativeAdvancedModelHelper(mActivity).loadNativeAdvancedAd(
            NativeAdsSize.Medium,
            mBinding.flNativeAdPlaceHolderMedium,
            isAddVideoOptions = intent?.extras?.getBoolean("is_add_video_options") ?: false,
        )
    }
}