package com.espresso.pbmobile.main.refueling

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.espresso.pbmobile.R
import com.espresso.pbmobile.databinding.ItemRefuelBinding
import com.espresso.pbmobile.main.refueling.RefuelItemModel.Companion.ITEM_REFUEL_95
import com.espresso.pbmobile.main.refueling.RefuelItemModel.Companion.ITEM_REFUEL_98
import com.espresso.pbmobile.main.refueling.RefuelItemModel.Companion.ITEM_REFUEL_LPG
import com.espresso.pbmobile.main.refueling.RefuelItemModel.Companion.ITEM_REFUEL_ON

class RefuelRecyclerAdapter(
    items: List<RefuelItemModel>
) : RecyclerView.Adapter<RefuelRecyclerAdapter.ViewHolder>() {

    private var itemList: MutableList<RefuelItemModel> = items.toMutableList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return ViewHolder(ItemRefuelBinding.inflate(inflater, parent, false))
    }

    override fun getItemCount() = itemList.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = itemList[position]
        holder.binding.model = item
    }

    override fun getItemId(position: Int) = itemList[position].id

    fun updateItems(items: List<RefuelItemModel>) {
        itemList.clear()
        itemList = items.toMutableList()
        notifyDataSetChanged()
    }

    class ViewHolder(val binding: ItemRefuelBinding) : RecyclerView.ViewHolder(binding.root)
}
