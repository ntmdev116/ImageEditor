package com.sun.imageeditor.screen.detail

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.sun.imageeditor.R
import com.sun.imageeditor.databinding.FragmentPhotoDetailBinding
import com.sun.imageeditor.utils.base.BaseFragment

class PhotoDetailFragment : BaseFragment<FragmentPhotoDetailBinding>(FragmentPhotoDetailBinding::inflate) {

    override fun initData() {
    }

    override fun initView() {

    }

    companion object {
        fun newInstance() =
            PhotoDetailFragment()
    }
}
