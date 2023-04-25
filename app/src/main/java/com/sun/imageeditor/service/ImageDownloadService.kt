package com.sun.imageeditor.service

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.media.MediaScannerConnection
import android.os.Build
import android.os.Environment
import android.os.IBinder
import android.util.Log
import androidx.core.app.NotificationCompat
import com.sun.imageeditor.R
import com.sun.imageeditor.screen.home.HomeActivity
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.InputStream
import java.net.HttpURLConnection
import java.net.URL
import java.util.concurrent.Executors

class ImageDownloadService : Service() {

    private val mExecutor by lazy { Executors.newCachedThreadPool() }
    private val notificationManager by lazy {
        getSystemService(Context.NOTIFICATION_SERVICE) as? NotificationManager
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val url = intent?.getStringExtra(DOWNLOAD_URL_KEY)
        val fileName = intent?.getStringExtra(DOWNLOAD_FILE_NAME_KEY)
        if (url != null) {
            createNotificationChannel()
            val notificationId = System.currentTimeMillis().toInt()
            updateProgressNotification(notificationId, 0)
            downloadImage(notificationId, url, fileName)
        }
        return super.onStartCommand(intent, flags, startId)
    }

    private fun downloadImage(notificationId: Int, imageUrl: String, fileName: String?) {
        mExecutor.execute {
            try {
                val url = URL(imageUrl)
                val connection = (url.openConnection() as HttpURLConnection)
                    .apply {
                        doInput = true
                        connectTimeout = CONNECT_TIMEOUT
                        readTimeout = READ_TIMEOUT
                    }
                connection.connect()

                val fileLength = connection.contentLength
                val input = connection.inputStream
                saveImage(notificationId, input, fileLength, fileName)
            } catch (e: IOException) {
                Log.e(LOG_TAG, getString(R.string.download_service_error), e)
                updateProgressNotification(notificationId, DOWNLOAD_FAIL_CODE)
            }
        }
    }

    private fun saveImage(
        notificationId: Int,
        input: InputStream,
        contentLength: Int,
        fileName: String? = null
    ) {
        val downloadDirectoryPath =
            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
        val appName = getString(R.string.app_name)
        val directory = File("$downloadDirectoryPath/$appName")
        if (!directory.exists()) {
            directory.mkdirs()
        }

        val saveName =
            fileName ?: getString(R.string.download_file_name, notificationId.toString())

        val file = File(directory, saveName)
        val output = FileOutputStream(file)

        val bufferSize = when {
            contentLength < IMAGE_SIZE_MEDIUM -> BUFFER_SIZE_MEDIUM
            contentLength < IMAGE_SIZE_LARGE -> BUFFER_SIZE_LARGE
            else -> BUFFER_SIZE_SUPER_LARGE
        }

        val buffer = ByteArray(bufferSize)
        var totalBytesRead = 0f
        var bytesRead: Int
        while (input.read(buffer).also { bytesRead = it } != -1) {
            totalBytesRead += bytesRead.toFloat()

            val progress = ((totalBytesRead / contentLength.toFloat()) * MAX_LENGTH_PROGRESS).toInt()
            updateProgressNotification(notificationId, progress)

            output.write(buffer, 0, bytesRead)
        }

        output.flush()
        output.close()
        input.close()

        MediaScannerConnection.scanFile(
            baseContext,
            arrayOf(file.absolutePath),
            null
        ) { _, _ ->
            // TODO
        }

    }

    private fun updateProgressNotification(notificationId: Int, progress: Int) {
        val title = getString(R.string.image_download_service_notification_title)
        val message = when (progress) {
            DOWNLOAD_FAIL_CODE -> getString(R.string.image_download_service_notification_message_fail)
            in 0 until MAX_LENGTH_PROGRESS -> getString(R.string.image_download_service_notification_message)
            else -> getString(R.string.image_download_service_notification_message_complete)
        }

        val builder = getNotificationBuilder(title, message)

        if (progress in 0 until MAX_LENGTH_PROGRESS) {
            builder.setProgress(MAX_LENGTH_PROGRESS, progress, false)
        } else {
            notificationManager?.cancel(notificationId)
            builder.setProgress(0, 0, false)
        }

        notificationManager?.notify(notificationId, builder.build())
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                NOTIFICATION_CHANNEL_ID,
                NOTIFICATION_CHANNEL_NAME,
                NotificationManager.IMPORTANCE_LOW
            )
            notificationManager?.createNotificationChannel(channel)
        }
    }

    private fun getNotificationBuilder(title: String, message: String): NotificationCompat.Builder {
        val notificationIntent = Intent(this, HomeActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(
            this,
            0,
            notificationIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        return NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID)
            .setContentTitle(title)
            .setContentText(message)
            .setSmallIcon(android.R.drawable.stat_sys_download_done)
            .setContentIntent(pendingIntent)
            .setAutoCancel(false)
    }

    companion object {
        const val CONNECT_TIMEOUT = 10000 // 10 seconds
        const val READ_TIMEOUT = 300000 // 5 minutes
        const val MAX_LENGTH_PROGRESS = 100
        const val IMAGE_SIZE_MEDIUM = 1024 * 1024
        const val IMAGE_SIZE_LARGE = 10 * 1024 * 1024
        const val BUFFER_SIZE_MEDIUM = 4096
        const val BUFFER_SIZE_LARGE = 16384
        const val BUFFER_SIZE_SUPER_LARGE = 40960
        const val DOWNLOAD_FAIL_CODE = -1
        const val LOG_TAG = "com.sun.imageeditor.ImageDownloadService"
        const val NOTIFICATION_CHANNEL_ID = "ImageDownloadServiceChannel"
        const val NOTIFICATION_CHANNEL_NAME = "Image Download Service"
        const val DOWNLOAD_URL_KEY = "DOWNLOAD_URL_KEY"
        const val DOWNLOAD_FILE_NAME_KEY = "DOWNLOAD_FILE_NAME_KEY"
    }
}
