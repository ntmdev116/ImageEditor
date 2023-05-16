package com.sun.imageeditor.screen.edit

import com.sun.imageeditor.R
import com.sun.imageeditor.databinding.FragmentIconBinding
import com.sun.imageeditor.screen.edit.adapter.IconAdapter
import com.sun.imageeditor.utils.EditType
import com.sun.imageeditor.utils.OnItemRecyclerViewClickListener
import com.sun.imageeditor.utils.base.BaseFragment

class IconFragment : BaseFragment<FragmentIconBinding>(
    FragmentIconBinding::inflate
), EditFragment {
    var emojiId: Int = R.drawable.emoji_cool
        private set

    private val mIconAdapter by lazy {
        IconAdapter().apply {
            setIconList(
                listOf(
                    R.drawable.emoji_cool,
                    R.drawable.emoji_phew,
                    R.drawable.emoji_stuned,
                    R.drawable.emojii_pleading,
                )
            )

            setOnItemClickListener(object : OnItemRecyclerViewClickListener<Int> {
                override fun onItemClick(parameter: Int?) {
                    parameter?.let {
                        emojiId = it
                    }
                }
            })
        }
    }

    override val editType: EditType
        get() = EditType.ICON

    override fun initData() {
        // TODO
    }

    override fun initView() {
        binding.recyclerIcon.adapter = mIconAdapter
    }

}
