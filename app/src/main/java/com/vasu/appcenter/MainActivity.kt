package com.vasu.appcenter

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import com.example.latest.vasu.newappcenter.MoreApps
import com.example.latest.vasu.newappcenter.base.BaseBindingActivity
import com.vasu.appcenter.databinding.ActivityMainBinding

class MainActivity : BaseBindingActivity<ActivityMainBinding>() {


    override fun getActivityContext(): AppCompatActivity {
        return this@MainActivity
    }

    override fun setBinding(layoutInflater: LayoutInflater): ActivityMainBinding {
        return ActivityMainBinding.inflate(layoutInflater)
    }

    override fun initView() {
        super.initView()
        MoreApps.with(mActivity)
            .setAppPackageName("com.vehicle.rto.vahan.status.information.register")
            .setTextColor(Color.BLUE)
            .setThemeColor(Color.RED)
            .setShareMessage("abc")
            .launch()
    }
}