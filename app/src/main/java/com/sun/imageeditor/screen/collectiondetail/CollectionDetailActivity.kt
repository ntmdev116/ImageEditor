package com.sun.imageeditor.screen.collectiondetail

import android.os.Handler
import android.os.Looper
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.sun.imageeditor.data.model.Photo
import com.sun.imageeditor.data.model.PhotoCollection
import com.sun.imageeditor.data.repository.PhotoCollectionRepository
import com.sun.imageeditor.data.repository.source.remote.PhotoCollectionRemoteSource
import com.sun.imageeditor.databinding.ActivityCollectionDetailBinding
import com.sun.imageeditor.screen.detail.PhotoDetailFragment
import com.sun.imageeditor.screen.home.adapter.HomeAdapter
import com.sun.imageeditor.utils.Constant
import com.sun.imageeditor.utils.OnItemRecyclerViewClickListener
import com.sun.imageeditor.utils.base.BaseActivity
import com.sun.imageeditor.utils.ext.addFragment

class CollectionDetailActivity :
    BaseActivity<ActivityCollectionDetailBinding>(
        ActivityCollectionDetailBinding::inflate
    ), CollectionDetailContract.View {
    private val mCollectionPhotosAdapter by lazy { HomeAdapter() }
    private val mPresenter by lazy {
        CollectionDetailPresenter(
            PhotoCollectionRepository(
                PhotoCollectionRemoteSource.getInstance()
            )
        )
    }

    override fun initView() {
        binding.recyclerHome.run {
            addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                    super.onScrollStateChanged(recyclerView, newState)

                    if (!recyclerView.canScrollVertically(1) && !mCollectionPhotosAdapter.isPhotoLoading) {
                        mCollectionPhotosAdapter.isPhotoLoading = true
                        mPresenter.getPhotos()
                    }
                }
            })

            adapter = mCollectionPhotosAdapter
        }
        binding.buttonBack.setOnClickListener { onBackPressed() }

        mCollectionPhotosAdapter.setOnPhotoClick(object : OnItemRecyclerViewClickListener<String> {
            override fun onItemClick(parameter: String?) {
                addFragment(binding.framePhotos.id, PhotoDetailFragment.newInstance())
            }
        })
        mPresenter.setView(this)
    }

    override fun initData() {
        mPresenter.getPhotos()
    }

    override fun onGetPhotoCollectionsSuccess(collections: MutableList<PhotoCollection>) {
        // TODO Not yet implemented
    }

    override fun onGetPhotosSuccess(photos: MutableList<Photo>) {
        mCollectionPhotosAdapter.addPhotos(photos)
        mCollectionPhotosAdapter.isPhotoLoading = false
    }

    override fun onError(msg: String?) {
        Handler(Looper.getMainLooper()).post {
            Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
            mCollectionPhotosAdapter.isPhotoLoading = false
        }
    }

    override fun getCollectionId(): String? = intent.extras?.getString(Constant.COLLECTION_ID_KEY)
}
