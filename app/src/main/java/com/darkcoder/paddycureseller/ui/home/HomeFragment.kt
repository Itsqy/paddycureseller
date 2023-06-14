package com.darkcoder.paddycureseller.ui.home

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.viewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.darkcoder.paddycure.data.network.ApiConfig
import com.darkcoder.paddycure.utils.ViewModelFactory
import com.darkcoder.paddycureseller.R
import com.darkcoder.paddycureseller.data.viewmodel.HomeViewModel
import com.darkcoder.paddycureseller.databinding.FragmentHomeBinding
import com.darkcoder.paddycureseller.ui.login.dataStore
import com.darkcoder.paddycureseller.utils.UserPreferences

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return  binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val homeViewModel: HomeViewModel by viewModels {
            ViewModelFactory(UserPreferences.getInstance(requireActivity().dataStore), ApiConfig)
        }

        homeViewModel.getUser().observe(viewLifecycleOwner) {
            homeViewModel.getProduct(it.userId)
        }

        val layoutManager = GridLayoutManager(requireActivity(), 2)
        binding.rvProduct.layoutManager = layoutManager

//        val homeViewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory()).get(
//            HomeViewModel::class.java
//        )

        homeViewModel.listProduct.observe(viewLifecycleOwner) {
            val adapter = ListProductAdapter(it)
            binding.rvProduct.adapter = adapter
        }
    }

}