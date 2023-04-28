package com.sun.imageeditor.utils

import android.content.DialogInterface
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.View
import androidx.appcompat.app.AlertDialog
import com.sun.imageeditor.R

class Dialog(
    private val builder: AlertDialog.Builder
) {

    private var _dialog: AlertDialog? = null

    fun showDialog(
        title: String,
        message: String,
        positiveMessage: String?,
        negativeMessage: String,
        onPositive: ((DialogInterface, Int) -> Unit)?
    ) {
        if (_dialog == null || _dialog?.isShowing == false) {
            builder.apply {
                setTitle(title)
                setMessage(message)
                setView(null)
                setCancelable(true)
                setNegativeButton(negativeMessage) { dialog, _ ->
                    dialog.cancel()
                }
                setPositiveButton(positiveMessage) { dialog, which ->
                    if (onPositive != null) {
                        onPositive(dialog, which)
                    }
                }
            }
            _dialog = builder.create()
            _dialog?.show()
        }
    }

    fun showLoadingDialog() {
        if (_dialog == null) {
            builder.setView(R.layout.layout_loading)
                .setCancelable(false)
            _dialog = builder.create()
            _dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            _dialog?.show()
        }
    }

    fun dismiss() {
        if (_dialog?.isShowing == true) {
            _dialog?.dismiss()
        }
    }
}
