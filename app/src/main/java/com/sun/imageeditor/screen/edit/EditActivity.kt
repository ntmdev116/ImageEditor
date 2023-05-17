package com.sun.imageeditor.screen.edit

import android.Manifest
import android.graphics.Bitmap
import android.graphics.Canvas
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.sun.imageeditor.R
import com.sun.imageeditor.databinding.ActivityEditBinding
import com.sun.imageeditor.screen.search.adapter.SearchViewPagerAdapter
import com.sun.imageeditor.utils.Dialog
import com.sun.imageeditor.utils.EditParameters
import com.sun.imageeditor.utils.EditType
import com.sun.imageeditor.utils.FilterType
import com.sun.imageeditor.utils.PointToDraw
import com.sun.imageeditor.utils.base.BaseActivity
import com.sun.imageeditor.utils.ext.loadOriginalImageWithUrl

class EditActivity : BaseActivity<ActivityEditBinding>(
    ActivityEditBinding::inflate
), EditContract.View {

    private val mPresenter by lazy { EditPresenter.getInstance() }

    private val mDialog by lazy {
        Dialog(AlertDialog.Builder(this, R.style.AlertDialogTheme))
    }

    private val editTypeToFragmentMap = mapOf(
        EditType.FILTER to FilterFragment(),
        EditType.ADJUST to AdjustFragment(),
        EditType.ICON to IconFragment(),
        EditType.DRAW to DrawFragment().also {
            it.setSettingChanged { (color, size) ->
                binding.imageMain.color = color
                binding.imageMain.size = size
            }
            it
        },
    )

    private var mCurrentEditType = EditType.ORIGINAL

    override fun onDestroy() {
        mPresenter.onStop()
        super.onDestroy()
    }

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

            val image = Bitmap.createBitmap(
                binding.imageMain.measuredWidth,
                binding.imageMain.measuredHeight,
                Bitmap.Config.ARGB_8888
            )

            val canvas = Canvas(image)

            binding.imageMain.draw(canvas)

            mPresenter.saveBitmap(this, binding.imageMain.clickPaths)
        }

        binding.imageMain.apply {
            setOnButtonDownListener {
                if (mCurrentEditType == EditType.ICON) {
                    val icon = (editTypeToFragmentMap[mCurrentEditType] as? IconFragment)?.emojiId

                    if (icon != null) {
                        mPresenter.onEditButtonClick(
                            mCurrentEditType,
                            EditParameters(icon = PointToDraw(it, icon))
                        )
                    }
                }
            }
        }

        val url = intent.getStringExtra(IMAGE_URL)
        url?.let {
            binding.imageMain.loadOriginalImageWithUrl(it, R.color.black, R.drawable.ic_error) {bm ->
                mPresenter.setBitmap(bm, this)

                // load filter preview when image ready
                FilterFragment.filterTypes.forEach { type ->
                    mPresenter.getFilterPreview(type)
                }
            }
        }
    }

    private fun setupTabLayout() {
        val fragments = editTypeToFragmentMap.values.filterIsInstance<Fragment>()
        val adapter = SearchViewPagerAdapter(
            supportFragmentManager,
            lifecycle
        ).apply { setTabList(fragments) }

        binding.viewPager.adapter = adapter
        binding.viewPager.isUserInputEnabled = false
        TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, position ->
            tab.text = (fragments[position] as? EditFragment)?.displayName
        }.attach()

        binding.tabLayout.run {
            addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
                override fun onTabSelected(tab: TabLayout.Tab?) {
                    (fragments[selectedTabPosition] as? EditFragment)?.let {
                        mCurrentEditType = it.editType
                    }

                    binding.imageMain.canDraw = fragments[selectedTabPosition] is DrawFragment
                }

                override fun onTabUnselected(tab: TabLayout.Tab?) {
                    // TODO
                }

                override fun onTabReselected(tab: TabLayout.Tab?) {
                    // TODO
                }

            })
        }
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

    override fun onGetFilterPreviewSuccess(filterType: FilterType, bitmap: Bitmap) {
        Handler(Looper.getMainLooper()).post {
            (editTypeToFragmentMap[EditType.FILTER] as? FilterFragment)?.onGetFilterPreview(
                filterType,
                bitmap
            )
        }
    }

    override fun onGetProcessedBitmap(bitmap: Bitmap) {
        Handler(Looper.getMainLooper()).post {
            binding.imageMain.setImageBitmap(bitmap)
            binding.imageMain.invalidate()
        }
    }

    companion object {

        const val IMAGE_URL = "IMAGE_URL"
        private const val WRITE_EXTERNAL_STORAGE = Manifest.permission.WRITE_EXTERNAL_STORAGE
    }
}
