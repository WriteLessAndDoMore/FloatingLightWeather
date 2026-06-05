package com.xiangyang.floatinglightweather.ui

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.xiangyang.floatinglightweather.R
import com.xiangyang.floatinglightweather.adapter.DistrictsAdapter
import com.xiangyang.floatinglightweather.databinding.FragmentDistrictsBinding
import com.xiangyang.floatinglightweather.viewmodel.DistrictViewModel


class DistrictsFragment : Fragment() {
    private val viewModel: DistrictViewModel by activityViewModels()
    private lateinit var binding: FragmentDistrictsBinding
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
            if (content.isNotEmpty()) {
                viewModel.searchLocation(content)
            } else {
                binding.rvDistricts.visibility = View.GONE
                binding.ivBg.visibility = View.VISIBLE
                viewModel.districtList.clear()
                adapter.notifyDataSetChanged()
            }
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun initObserve() {
        viewModel.subDistrictResult.observe(viewLifecycleOwner) { districts ->
            districts?.let {
                binding.rvDistricts.visibility = View.VISIBLE
                binding.ivBg.visibility = View.GONE
                viewModel.districtList.clear()
                viewModel.districtList.addAll(districts)
                adapter.notifyDataSetChanged()
            } ?: Toast.makeText(activity, "未查询到任何地点", Toast.LENGTH_LONG).show()
        }

    }

    private fun initRecyclerView() {
        val layoutManager = LinearLayoutManager(activity)
        binding.rvDistricts.layoutManager = layoutManager
        adapter = DistrictsAdapter(this, viewModel.districtList)
        binding.rvDistricts.adapter = adapter
    }

}