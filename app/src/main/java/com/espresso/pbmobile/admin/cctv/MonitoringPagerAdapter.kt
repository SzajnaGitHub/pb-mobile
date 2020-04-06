package com.espresso.pbmobile.admin.cctv

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.core.content.ContextCompat
import androidx.viewpager.widget.PagerAdapter
import com.espresso.pbmobile.R

data class MonitoringPagerAdapter(val context: Context) : PagerAdapter() {

    private val monitoringItems = listOf(
        R.drawable.monitoring_1,
        R.drawable.monitoring_2,
        R.drawable.monitoring_3,
        R.drawable.monitoring_4
    )

    override fun isViewFromObject(view: View, `object`: Any) = view == `object`

    override fun getCount() = monitoringItems.size

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val image = ImageView(context)
        image.setImageDrawable(ContextCompat.getDrawable(context, monitoringItems[position]))
        container.addView(image)
        return image
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(`object` as View)
    }
}
