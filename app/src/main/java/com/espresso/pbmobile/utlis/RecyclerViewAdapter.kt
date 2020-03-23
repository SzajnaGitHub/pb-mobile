package com.espresso.pbmobile.utlis

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView

class RecyclerViewAdapter<T>(
    private val items: List<T>,
    private val layoutId: Int,
    private val bindingVariableId: Int
) : RecyclerView.Adapter<RecyclerViewAdapter.BindingViewHolder<ViewDataBinding>>() {

    private var itemList: MutableList<T> = items.toMutableList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BindingViewHolder<ViewDataBinding> {
        val inflater = LayoutInflater.from(parent.context)
        val binding = DataBindingUtil.inflate<ViewDataBinding>(inflater, layoutId, parent, false)
        return BindingViewHolder(binding)
    }

    override fun getItemCount() = items.size

    override fun onBindViewHolder(holder: BindingViewHolder<ViewDataBinding>, position: Int) {
        val item = items[position]
        holder.binding.setVariable(bindingVariableId, item)
    }

    fun updateItems(items: List<T>) {
        itemList.clear()
        itemList = items.toMutableList()
        notifyDataSetChanged()
    }

    class BindingViewHolder<out Binding : ViewDataBinding>(val binding: Binding) : RecyclerView.ViewHolder(binding.root)
}
