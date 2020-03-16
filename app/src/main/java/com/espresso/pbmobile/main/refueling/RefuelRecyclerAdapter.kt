package com.espresso.pbmobile.main.refueling

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.espresso.pbmobile.databinding.ItemRefuelBinding

class RefuelRecyclerAdapter(
    private val items: List<RefuelItemModel>
) : RecyclerView.Adapter<RefuelRecyclerAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return ViewHolder(ItemRefuelBinding.inflate(inflater, parent, false))
    }

    override fun getItemCount() = items.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]
        holder.binding.model = item
    }

    class ViewHolder(val binding: ItemRefuelBinding) : RecyclerView.ViewHolder(binding.root)
}
