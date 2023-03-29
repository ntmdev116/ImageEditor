package com.sun.imageeditor.screen.home

import com.sun.imageeditor.databinding.FragmentHomeBinding
import com.sun.imageeditor.screen.home.adapter.CollectionCoverPhotoAdapter
import com.sun.imageeditor.screen.home.adapter.PhotoAdapter
import com.sun.imageeditor.utils.base.BaseFragment

class HomeFragment : BaseFragment<FragmentHomeBinding>(FragmentHomeBinding::inflate) {
    private val mCollectionAdapter by lazy { CollectionCoverPhotoAdapter() }
    private val mPhotoAdapter by lazy { PhotoAdapter() }

    override fun initData() {
        binding.recyclerCollection.adapter = mCollectionAdapter
        binding.recyclerPhoto.adapter = mPhotoAdapter
    }

    companion object {
        fun newInstance() =
            HomeFragment()
    }
}
