package com.sun.imageeditor.screen.detail

import com.sun.imageeditor.databinding.FragmentPhotoDetailBinding
import com.sun.imageeditor.utils.base.BaseFragment

class PhotoDetailFragment : BaseFragment<FragmentPhotoDetailBinding>(FragmentPhotoDetailBinding::inflate) {

    override fun initData() {
    }

    override fun initView() {
        binding.buttonBack.setOnClickListener { activity?.onBackPressed() }
        binding.root.isClickable = true
    }

    companion object {
        fun newInstance() =
            PhotoDetailFragment()
    }
}
