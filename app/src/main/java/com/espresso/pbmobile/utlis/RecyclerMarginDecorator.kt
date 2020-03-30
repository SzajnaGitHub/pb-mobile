package com.espresso.pbmobile.utlis

import android.content.Context
import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.espresso.pbmobile.R
import kotlin.math.roundToInt

class RecyclerMarginDecorator(val context: Context) : RecyclerView.ItemDecoration() {
    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        super.getItemOffsets(outRect, view, parent, state)
        val marginSmall = context.resources.getDimension(R.dimen.margin_small).roundToInt()
        val marginMedium = context.resources.getDimension(R.dimen.margin_medium).roundToInt()
        outRect.set(marginMedium, marginSmall, marginMedium, marginSmall)
    }
}
