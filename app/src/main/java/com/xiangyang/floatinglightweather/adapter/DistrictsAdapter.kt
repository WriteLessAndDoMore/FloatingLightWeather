package com.xiangyang.floatinglightweather.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.xiangyang.floatinglightweather.data.bean.District
import com.xiangyang.floatinglightweather.databinding.ItemDistrictsBinding

class DistrictsAdapter(private val fragment: Fragment, private val districtsList: List<District>) :
    RecyclerView.Adapter<DistrictsAdapter.DistrictsViewHolder>() {
    override fun onCreateViewHolder(
        parent: ViewGroup, viewType: Int
    ): DistrictsViewHolder {
        val binding =
            ItemDistrictsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return DistrictsViewHolder(binding)
    }

    override fun onBindViewHolder(
        viewHolder: DistrictsViewHolder, position: Int
    ) {
        val district = districtsList[position]
        viewHolder.binding.tvDistrictsName.text = district.name
    }

    override fun getItemCount(): Int = districtsList.size

    class DistrictsViewHolder(val binding: ItemDistrictsBinding) :
        RecyclerView.ViewHolder(binding.root)
}