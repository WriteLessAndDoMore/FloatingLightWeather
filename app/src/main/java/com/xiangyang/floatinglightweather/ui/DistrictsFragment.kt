package com.xiangyang.floatinglightweather.ui

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.xiangyang.floatinglightweather.adapter.DistrictsAdapter
import com.xiangyang.floatinglightweather.constant.GDConstant
import com.xiangyang.floatinglightweather.databinding.FragmentDistrictsBinding
import com.xiangyang.floatinglightweather.util.showToast
import com.xiangyang.floatinglightweather.viewmodel.DistrictViewModel
import com.xiangyang.floatinglightweather.util.startActivity


class DistrictsFragment : Fragment() {
    private val viewModel: DistrictViewModel by activityViewModels()
    lateinit var binding: FragmentDistrictsBinding
    private lateinit var adapter: DistrictsAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentDistrictsBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initRecyclerView()
        searchLocation()
        initObserve()
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun searchLocation() {
        binding.etSearch.addTextChangedListener {
            val content = it.toString()
            viewModel.searchLocation(content)
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun initObserve() {
        viewModel.subDistrictResult.observe(viewLifecycleOwner) { districts ->
            if (!districts.isNullOrEmpty()) {
                binding.rvDistricts.visibility = View.VISIBLE
                binding.ivBg.visibility = View.GONE
                viewModel.districtList.clear()
                viewModel.districtList.addAll(districts)
                adapter.notifyDataSetChanged()
            } else {
                binding.rvDistricts.visibility = View.GONE
                binding.ivBg.visibility = View.VISIBLE
                viewModel.districtList.clear()
                adapter.notifyDataSetChanged()
                if (binding.etSearch.text.isNotBlank()) "未查询到任何地点".showToast()
            }

        }

    }

    private fun initRecyclerView() {
        val layoutManager = LinearLayoutManager(activity)
        binding.rvDistricts.layoutManager = layoutManager
        adapter = DistrictsAdapter(viewModel.districtList) { district ->
            val adCode = district.adCode
            val activity = activity
            if (activity is WeatherActivity) {
                activity.binding.dlWeather.closeDrawers()
                activity.refreshAndGetWeatherAllInfo(adCode)
                activity.currentAdCode = adCode
                binding.etSearch.text.clear()
            } else {
                startActivity<WeatherActivity> {
                    putExtra(GDConstant.GeneralConstant.IntentKey.CITY_AD_CODE, adCode)
                }
                activity?.finish()
            }
        }
        binding.rvDistricts.adapter = adapter
    }

}