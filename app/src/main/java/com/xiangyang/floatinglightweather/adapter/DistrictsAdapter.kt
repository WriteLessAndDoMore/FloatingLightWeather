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

class DistrictsAdapter(private val fragment: Fragment, private val districtsList: List<District>) :
    RecyclerView.Adapter<DistrictsAdapter.DistrictsViewHolder>() {
    override fun onCreateViewHolder(
        parent: ViewGroup, viewType: Int
    ): DistrictsViewHolder {
        val binding =
            ItemDistrictsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        val viewHolder = DistrictsViewHolder(binding)
        viewHolder.itemView.setOnClickListener {
            val district = districtsList[viewHolder.bindingAdapterPosition]
            val adCode = district.adCode as String
            val activity = fragment.activity
            if (activity is WeatherActivity && fragment is DistrictsFragment) {
                activity.binding.dlWeather.closeDrawers()
                activity.refreshAndGetWeatherAllInfo(adCode)
                activity.currentAdCode = adCode
                fragment.binding.etSearch.text.clear()
            } else {
                val intent = Intent(parent.context, WeatherActivity::class.java)
                intent.putExtra(GDConstant.GeneralConstant.IntentKey.CITY_AD_CODE,adCode)
                fragment.startActivity(intent)
                fragment.activity?.finish()
            }


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