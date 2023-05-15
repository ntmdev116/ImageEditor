package com.sun.imageeditor.screen.edit

import android.content.Context
import android.graphics.Bitmap
import com.sun.imageeditor.utils.FilterType

interface EditContract {
    interface View {
        fun onSaveSuccess()
        fun onGetProcessedBitmap(bitmap: Bitmap)
        fun onSaveFail(msg: String?)
        fun onGetFilterPreviewSuccess(filterType: FilterType, bitmap: Bitmap)
    }

    interface Presenter {
        fun setBitmap(bitmap: Bitmap)
        fun saveBitmap(context: Context?)
        fun setView(view: View)
        fun onStop()
    }
}
