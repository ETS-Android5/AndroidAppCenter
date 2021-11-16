package com.example.latest.vasu.newappcenter.adapter

import android.content.Context
import android.os.SystemClock
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.app.base.BaseAdapter
import com.example.app.base.BaseViewHolder
import com.example.app.base.utils.isValidContextForGlide
import com.example.latest.vasu.newappcenter.R
import com.example.latest.vasu.newappcenter.databinding.ListItemBackAppsBinding
import com.example.latest.vasu.newappcenter.model.Data
import com.example.latest.vasu.newappcenter.themeColor
import com.example.latest.vasu.newappcenter.utils.*


open class BackAppsAdapter(
    private val mContext: Context,
    private val mApps: MutableList<Data>
) : BaseAdapter<Data>(mApps) {

    // variable to track event time
    var mLastClickTime: Long = 0
    private val mMinDuration = 1500

    inner class MyViewHolder(fBinding: ListItemBackAppsBinding) : BaseViewHolder<ListItemBackAppsBinding>(fBinding)

    override fun onCreateHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<*> {
        return MyViewHolder(
            ListItemBackAppsBinding.inflate(
                LayoutInflater.from(mContext),
                parent,
                false
            )
        )
    }

    override fun onBindHolder(holder: BaseViewHolder<*>, position: Int) {
        if (mContext.isValidContextForGlide) {

            with(holder as MyViewHolder) {
                with(fBinding) {
                    with(mApps[position]) {
                        Glide.with(itemView)
                            .load(thumbImage)
                            .placeholder(R.drawable.thumb_small)
                            .thumbnail(0.15f)
                            .into(listAppsIvThumb)

                        itemView.setOnClickListener {
                            // Preventing multiple clicks, using threshold of mMinDuration second
                            if (SystemClock.elapsedRealtime() - mLastClickTime < mMinDuration) {
                                return@setOnClickListener
                            }
                            mLastClickTime = SystemClock.elapsedRealtime()

                            mContext.rateApp(packageName)
                        }

                        themeColor?.let {
                            listAppsIvAd.setColorFilter(it, android.graphics.PorterDuff.Mode.SRC_IN)
                            listAppsTvAppName.background = mContext.shapeCategorySelectedDrawable
                        }
                    }
                }
            }
        }
    }


}