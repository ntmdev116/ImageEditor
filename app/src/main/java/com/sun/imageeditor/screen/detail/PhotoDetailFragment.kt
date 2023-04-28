package com.sun.imageeditor.screen.detail

import android.content.Intent
import android.os.Bundle
import com.sun.imageeditor.R
import com.sun.imageeditor.databinding.FragmentPhotoDetailBinding
import com.sun.imageeditor.screen.edit.EditActivity
import com.sun.imageeditor.utils.base.BaseFragment
import com.sun.imageeditor.utils.ext.loadOriginalImageWithUrl

class PhotoDetailFragment : BaseFragment<FragmentPhotoDetailBinding>(FragmentPhotoDetailBinding::inflate) {

    private var mImageUrl: String? = null
    private val mPresenter by lazy { PhotoDetailPresenter() }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            mImageUrl = it.getString(IMAGE_URL)
        }
    }

    override fun initData() {
        mImageUrl?.let {
            binding.imagePhoto.loadOriginalImageWithUrl(it, R.color.black, R.drawable.ic_error)
        }
    }

    override fun initView() {
        binding.buttonBack.setOnClickListener { activity?.onBackPressed() }
        binding.linearDownload.setOnClickListener {
            mImageUrl?.let { url ->
                mPresenter.downloadImage(context, url)
            }
        }
        binding.linearEdit.setOnClickListener {
            mImageUrl?.let { url ->
                startActivity(
                    Intent(context, EditActivity::class.java).apply {
                        putExtra(EditActivity.IMAGE_URL, url)
                    }
                )
            }
        }

        binding.imagePhoto.maxZoom = MAX_ZOOM
    }

    companion object {
        private const val IMAGE_URL = "IMAGE_URL"
        const val MAX_ZOOM = 20f
        fun newInstance(url: String) =
            PhotoDetailFragment().apply {
                arguments = Bundle().apply {
                    putString(IMAGE_URL, url)
                }
            }
    }
}
