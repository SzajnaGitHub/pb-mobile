package com.espresso.pbmobile.utlis

import androidx.annotation.LayoutRes
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.espresso.pbmobile.BR

object RecyclerBindingAdapter {

    @JvmStatic
    @BindingAdapter("items", "layoutId", "bindingVariableName")
    fun <T> bindRecyclerItem(
        recyclerView: RecyclerView,
        items: List<T>,
        @LayoutRes layoutId: Int,
        bindingVariableName: String
    ) {
        val bindingVariableId = BR::class.java.getDeclaredField(bindingVariableName).getInt(null)
        recyclerView.adapter = RecyclerViewAdapter(
            items = items,
            layoutId = layoutId,
            bindingVariableId = bindingVariableId
        )
        (recyclerView.adapter as RecyclerViewAdapter<T>).updateItems(items)
    }
}
