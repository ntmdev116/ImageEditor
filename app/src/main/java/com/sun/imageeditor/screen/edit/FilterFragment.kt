package com.sun.imageeditor.screen.edit

import com.sun.imageeditor.databinding.FragmentFilterBinding
import com.sun.imageeditor.screen.edit.adapter.FilterAdapter
import com.sun.imageeditor.utils.EditType
import com.sun.imageeditor.utils.FilterType
import com.sun.imageeditor.utils.base.BaseFragment

class FilterFragment : BaseFragment<FragmentFilterBinding>(
    FragmentFilterBinding::inflate
), EditFragment {

    override val editType: EditType
        get() = EditType.FILTER

    override fun initData() {
        // TODO not yet implemented
    }

    override fun initView() {
        binding.recyclerViewFilterChooser.adapter =
            FilterAdapter().apply {
                setFilterList(listOf(
                    FilterType.ORIGIN,
                    FilterType.VIVID,
                    FilterType.DESSERT,
                    FilterType.HONEY,
                ))
            }
    }
}
