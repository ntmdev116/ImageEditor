package com.sun.imageeditor.utils.base

import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.viewbinding.ViewBinding
import com.sun.imageeditor.R
import com.sun.imageeditor.utils.Dialog

abstract class BaseActivity<VB : ViewBinding>(
    private val bindingInflater: (inflater: LayoutInflater) -> VB
) : AppCompatActivity() {

    private var permissionLauncher: ActivityResultLauncher<String>? = null
    private val mDialog by lazy {
        Dialog(AlertDialog.Builder(this, R.style.AlertDialogTheme))
    }

    private var _binding: VB? = null
    val binding: VB
        get() = _binding as VB

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = bindingInflater.invoke(layoutInflater)
        setContentView(binding.root)
        initView()
        initData()
    }

    abstract fun initView()
    abstract fun initData()

    fun hideKeyboard() {
        val inputMethodManager = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(currentFocus?.windowToken, 0)
    }

    fun checkPermission(permission: String) {
        if (ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
            permissionLauncher = registerForActivityResult(ActivityResultContracts.RequestPermission()) { granted ->
                if (!granted) {
                    onUserResponsePermissionRequest(permission)
                }
            }

            permissionLauncher?.launch(permission)
        }
    }

    private fun onUserResponsePermissionRequest(permission: String) {
        if (shouldShowRequestPermissionRationale(permission)) {
            mDialog.showDialog(
                title = getString(R.string.permission_request_title),
                message = getString(R.string.permission_request_message),
                positiveMessage = getString(R.string.allow),
                negativeMessage = getString(R.string.text_cancel),
            ) { _, _ ->
                permissionLauncher?.launch(permission)
            }
        } else if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.Q) {
            Toast.makeText(this, getString(R.string.permission_request_denied), Toast.LENGTH_SHORT).show()
        }
    }

}
