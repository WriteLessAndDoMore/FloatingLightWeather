package com.xiangyang.floatinglightweather.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.xiangyang.floatinglightweather.constant.GDConstant
import com.xiangyang.floatinglightweather.data.bean.District
import com.xiangyang.floatinglightweather.databinding.ItemDistrictsBinding
import com.xiangyang.floatinglightweather.ui.DistrictsFragment
import com.xiangyang.floatinglightweather.ui.WeatherActivity

typealias DistrictCallback = (district: District) -> Unit
class DistrictsAdapter(private val districtsList: List<District>, private val districtCallback: DistrictCallback) :
    RecyclerView.Adapter<DistrictsAdapter.DistrictsViewHolder>() {
    override fun onCreateViewHolder(
        parent: ViewGroup, viewType: Int
    ): DistrictsViewHolder {
        val binding =
            ItemDistrictsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        val viewHolder = DistrictsViewHolder(binding)
        viewHolder.itemView.setOnClickListener {
            districtCallback(districtsList[viewHolder.bindingAdapterPosition])
        }
        return viewHolder
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