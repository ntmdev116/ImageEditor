package com.sun.imageeditor.screen.edit

import android.graphics.Bitmap
import com.sun.imageeditor.databinding.FragmentFilterBinding
import com.sun.imageeditor.screen.edit.adapter.FilterAdapter
import com.sun.imageeditor.utils.EditParameters
import com.sun.imageeditor.utils.EditType
import com.sun.imageeditor.utils.FilterType
import com.sun.imageeditor.utils.OnItemRecyclerViewClickListener
import com.sun.imageeditor.utils.base.BaseFragment

class FilterFragment : BaseFragment<FragmentFilterBinding>(
    FragmentFilterBinding::inflate
), EditFragment {

    private val mPresenter by lazy { EditPresenter.getInstance() }

    private val mFilterAdapter by lazy {
        FilterAdapter().apply {
            setFilterList(filterTypes)
            setOnItemClickListener(object : OnItemRecyclerViewClickListener<FilterType> {
                override fun onItemClick(parameter: FilterType?) {
                    mPresenter.onEditButtonClick(editType, EditParameters(filterType = parameter))
                }

            })
        }
    }

    override val editType: EditType
        get() = EditType.FILTER

    override fun initData() {
        // TODO
    }

    override fun initView() {
        binding.recyclerViewFilterChooser.adapter = mFilterAdapter
    }

    fun onGetFilterPreview(filterType: FilterType, bitmap: Bitmap) {
        mFilterAdapter.setPreviewBitmap(filterType, bitmap)
    }

    companion object {
        val filterTypes by lazy {
            listOf(
                FilterType.ORIGIN,
                FilterType.GRAYSCALE,
                FilterType.SEPIA,
                FilterType.PIXELATE,
                FilterType.SNOW,
                FilterType.VIGNETTE,
            )
        }
    }
}
