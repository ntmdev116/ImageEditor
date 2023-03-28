package com.sun.imageeditor.screen.home

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.sun.imageeditor.R
import com.sun.imageeditor.databinding.FragmentPhotoCollectionBinding
import com.sun.imageeditor.utils.base.BaseFragment

class PhotoCollectionFragment : BaseFragment<FragmentPhotoCollectionBinding>(FragmentPhotoCollectionBinding::inflate) {

    override fun initData() {
    }

    companion object {
        fun newInstance() =
            PhotoCollectionFragment()
    }
}
