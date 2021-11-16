package com.example.app.adshelper.dialogs

import android.app.Activity
import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.util.Log
import android.view.*
import com.example.app.adshelper.*
import com.example.app.adshelper.databinding.DialogFullScreenNativeAdBinding
import com.example.app.adshelper.visible

class FullScreenNativeAdDialog(
    private val activity: Activity,
    private val onDialogDismiss : () -> Unit = {}
) : Dialog(activity, R.style.full_screen_dialog) {

    private val TAG = javaClass.simpleName

    private var mBinding: DialogFullScreenNativeAdBinding

    init {
        requestWindowFeature(Window.FEATURE_NO_TITLE)

        mBinding = DialogFullScreenNativeAdBinding.inflate(LayoutInflater.from(context))
        setContentView(mBinding.root)

        window?.let {

            it.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            it.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)

            ///////////   Animation  ////////
            val lp = WindowManager.LayoutParams()
            lp.copyFrom(it.attributes)
            lp.width = WindowManager.LayoutParams.MATCH_PARENT
            lp.height = WindowManager.LayoutParams.MATCH_PARENT
            lp.gravity = Gravity.BOTTOM
            lp.windowAnimations = R.style.dialog_animation
            it.attributes = lp

        }

        setCancelable(false)
        setCanceledOnTouchOutside(false)

        setOnDismissListener {
            mBinding.flNativeAdPlaceHolder.removeAllViews()
        }

        mBinding.ivCloseAd.setOnClickListener {
            dismiss()
            onDialogDismiss.invoke()
        }
    }

    fun showFullScreenNativeAdDialog() {
        if (!activity.isFinishing && !isShowing && activity.isOnline) {
            Log.i(TAG, "showFullScreenNativeAdDialog: ")

            mBinding.ivCloseAd.visibility = View.GONE

            NativeAdvancedHelper.loadNativeAdvancedAd(
                fContext = activity,
                fSize = NativeAdsSize.FullScreen,
                fLayout = mBinding.flNativeAdPlaceHolder,
                isAdLoaded = { isNeedToRemoveCloseButton ->
                    if (!isNeedToRemoveCloseButton) {
                        mBinding.ivCloseAd.visibility = View.VISIBLE
                    }
                    mBinding.flNativeAdPlaceHolder.visible
                },
                onClickAdClose = {
                    mBinding.ivCloseAd.performClick()
                }
            )
            show()
        }
    }
}