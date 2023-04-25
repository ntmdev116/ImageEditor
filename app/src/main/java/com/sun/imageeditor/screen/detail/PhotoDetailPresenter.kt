package com.sun.imageeditor.screen.detail

import com.sun.imageeditor.service.ImageDownloadService
import android.content.Context
import android.content.Intent
import com.sun.imageeditor.utils.base.BasePresenter

class PhotoDetailPresenter : BasePresenter, PhotoDetailContract.Presenter {
    override fun downloadImage(context: Context?, url: String) {
        val intent = Intent(context, ImageDownloadService::class.java)
        intent.putExtra(ImageDownloadService.DOWNLOAD_URL_KEY, url)
        context?.run {
            startService(intent)
        }
    }

    override fun onStart() {
        // TODO Not yet implemented
    }

    override fun onStop() {
        // TODO Not yet implemented
    }
}
