package com.sun.imageeditor.screen.edit

import android.content.Context
import android.graphics.Bitmap
import com.sun.imageeditor.data.repository.source.OnResultListener
import com.sun.imageeditor.utils.ImageProcessor

class EditPresenter : EditContract.Presenter {
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
        mImageProcessor = ImageProcessor(bitmap)
    }

    override fun setView(view: EditContract.View) {
        mView = view
    }

    companion object {
        const val NOT_YET_DOWNLOADED = "Photo not yet downloaded"
    }
}
