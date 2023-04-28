package com.sun.imageeditor.screen.edit

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.google.android.material.tabs.TabLayoutMediator
import com.sun.imageeditor.R
import com.sun.imageeditor.databinding.ActivityEditBinding
import com.sun.imageeditor.screen.search.adapter.SearchViewPagerAdapter
import com.sun.imageeditor.utils.Dialog
import com.sun.imageeditor.utils.EditType
import com.sun.imageeditor.utils.FilterType
import com.sun.imageeditor.utils.base.BaseActivity
import com.sun.imageeditor.utils.ext.loadOriginalImageWithUrl

class EditActivity : BaseActivity<ActivityEditBinding>(
    ActivityEditBinding::inflate
), EditContract.View {
    private var permissionLauncher: ActivityResultLauncher<String>? = null

    private val mPresenter by lazy { EditPresenter() }
    private val mDialog by lazy {
        Dialog(AlertDialog.Builder(this, R.style.AlertDialogTheme))
    }

    private val editTypeToFragmentMap = mapOf(
        EditType.FILTER to FilterFragment(),
    )

    override fun initView() {
        checkPermission(WRITE_EXTERNAL_STORAGE)
        setupTabLayout()

        binding.buttonCancel.setOnClickListener {
            mDialog.showDialog(
                title = getString(R.string.title_edit_dialog),
                message = getString(R.string.message_edit_dialog),
                positiveMessage = getString(R.string.text_discard),
                negativeMessage = getString(R.string.text_keep_editing)
            ) { _, _ ->
                finish()
            }
        }
        binding.buttonDownload.setOnClickListener {
            mDialog.showLoadingDialog()
            mPresenter.saveBitmap(this)
        }

        val url = intent.getStringExtra(IMAGE_URL)
        url?.let {
            binding.imageMain.loadOriginalImageWithUrl(it, R.color.black, R.drawable.ic_error) {bm ->
                mPresenter.setBitmap(bm)
            }
        }
    }

    private fun setupTabLayout() {
        val list: List<Fragment> = editTypeToFragmentMap.values.toList()
        val adapter = SearchViewPagerAdapter(
            supportFragmentManager,
            lifecycle
        ).apply { setTabList(list) }
        binding.viewPager.adapter = adapter
        binding.viewPager.isUserInputEnabled = false
        TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, position ->
            tab.text = (list[position] as? EditFragment)?.displayName
        }.attach()
    }

    override fun initData() {
        mPresenter.setView(this)
    }

    override fun onSaveSuccess() {
        mDialog.dismiss()
        finish()
    }

    override fun onSaveFail(msg: String?) {
        mDialog.dismiss()
    }

    override fun onGetFilterPreviewSuccess(filterType: FilterType) {
        // TODO Not yet implemented
    }

    private fun checkPermission(permission: String) {
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

    companion object {
        const val IMAGE_URL = "IMAGE_URL"
        const val REQUEST_PERMISSIONS_CODE_WRITE_STORAGE = 101
        private const val WRITE_EXTERNAL_STORAGE = Manifest.permission.WRITE_EXTERNAL_STORAGE
    }
}
