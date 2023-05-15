package com.sun.imageeditor.screen.edit

import android.content.Context
import android.graphics.Bitmap
import com.sun.imageeditor.data.repository.source.OnResultListener
import com.sun.imageeditor.utils.EditParameters
import com.sun.imageeditor.utils.EditType
import com.sun.imageeditor.utils.FilterType
import com.sun.imageeditor.utils.ImageProcessor

class EditPresenter private constructor() : EditContract.Presenter {
    private var mImageProcessor: ImageProcessor? = null
    private var mView: EditContract.View? = null

    override fun saveBitmap(context: Context?) {
        if (mImageProcessor != null) {
            mImageProcessor!!.saveBitmap(
                fileName = null,
                context,
                object : OnResultListener<String> {
                    override fun onSuccess(result: String) {
                        mView?.onSaveSuccess()
                    }

                    override fun onFail(msg: String) {
                        mView?.onSaveFail(msg)
                    }
                }
            )
        } else {
            mView?.onSaveFail(NOT_YET_DOWNLOADED)
        }

    }

    override fun setBitmap(bitmap: Bitmap) {
        mImageProcessor = ImageProcessor(bitmap).apply {
            onResultListener = object : OnResultListener<Bitmap> {
                override fun onSuccess(result: Bitmap) {
                    mView?.onGetProcessedBitmap(result)
                }

                override fun onFail(msg: String) {
                    // TODO
                }
            }
        }
    }

    override fun setView(view: EditContract.View) {
        mView = view
    }

    override fun onStop() {
        mView = null
        mImageProcessor = null
    }

    fun getFilterPreview(filterType: FilterType) {
        mImageProcessor?.getFilterPreview(
            filterType,
            object : OnResultListener<Bitmap> {
                override fun onSuccess(result: Bitmap) {
                    mView?.onGetFilterPreviewSuccess(filterType, result)
                }

                override fun onFail(msg: String) {
                    // TODO
                }
            }
        )
    }

    fun onEditButtonClick(editType: EditType, editParameters: EditParameters) {
        mImageProcessor?.edit(editType, editParameters)
    }

    companion object {
        const val NOT_YET_DOWNLOADED = "Photo not yet downloaded"

        private var instance: EditPresenter? = null

        fun getInstance() =
            synchronized(this) {
                instance ?: EditPresenter().also {
                    instance = it
                }
            }
    }
}
