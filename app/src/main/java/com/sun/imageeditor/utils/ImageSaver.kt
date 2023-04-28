package com.sun.imageeditor.utils

import android.content.Context
import android.graphics.Bitmap
import android.media.MediaScannerConnection
import android.os.Environment
import com.sun.imageeditor.R
import com.sun.imageeditor.data.repository.source.OnResultListener
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.util.concurrent.Executors

class ImageSaver {

    fun saveBitmap(
        bm: Bitmap,
        fileName: String?,
        context: Context?,
        onResultListener: OnResultListener<String>,
    ) {
        context?.let {
            try {
                val downloadDirectoryPath =
                    Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
                val appName = it.getString(R.string.app_name)
                val directory = File("$downloadDirectoryPath/$appName")
                if (!directory.exists()) {
                    directory.mkdirs()
                }

                val saveName = fileName ?: it.getString(
                    R.string.download_file_name,
                    System.currentTimeMillis().toString()
                )

                val file = File(directory, saveName)
                val out = FileOutputStream(file)
                bm.compress(Bitmap.CompressFormat.JPEG, JPEG_COMPRESSION_QUALITY, out)
                out.flush()
                out.close()

                MediaScannerConnection.scanFile(
                    it,
                    arrayOf(file.absolutePath),
                    null
                ) { _, _ ->
                    onResultListener.onSuccess(saveName)
                }
            } catch (e: IOException) {
                onResultListener.onFail(e.message.toString())
            }
        }
    }

    companion object {
        private const val JPEG_COMPRESSION_QUALITY = 100
    }
}
