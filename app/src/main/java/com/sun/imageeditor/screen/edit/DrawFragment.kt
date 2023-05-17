package com.sun.imageeditor.screen.edit

import android.graphics.Color
import android.widget.SeekBar
import com.sun.imageeditor.R
import com.sun.imageeditor.databinding.FragmentDrawBinding
import com.sun.imageeditor.screen.edit.adapter.DrawAdapter
import com.sun.imageeditor.utils.EditType
import com.sun.imageeditor.utils.OnItemRecyclerViewClickListener
import com.sun.imageeditor.utils.base.BaseFragment

class DrawFragment : BaseFragment<FragmentDrawBinding>(
    FragmentDrawBinding::inflate
), EditFragment {
    var colorId: Int = R.color.black
        private set

    var brushSize: Int = 1
        private set

    private var onSettingChanged: ((Pair<Int, Int>) -> Unit)? = null

    fun setSettingChanged(onSettingChangeListener: ((Pair<Int, Int>) -> Unit)) {
        onSettingChanged = onSettingChangeListener
    }

    private val mColorAdapter by lazy {
        DrawAdapter().apply {
            setColorList(
                listOf(
                    Color.BLACK,
                    Color.BLUE,
                    Color.CYAN,
                    Color.GREEN,
                    Color.RED,
                    Color.WHITE,
                    Color.YELLOW,
                )
            )

            setOnItemClickListener(object : OnItemRecyclerViewClickListener<Int> {
                override fun onItemClick(parameter: Int?) {
                    parameter?.let {
                        colorId = it
                        onSettingChanged?.invoke(Pair(colorId, brushSize))
                    }
                }
            })
        }
    }

    override val editType: EditType
        get() = EditType.DRAW

    override fun initData() {
        // TODO
    }

    override fun initView() {
        binding.recyclerColor.adapter = mColorAdapter

        binding.seekBarBrushSize.max = MAX_BRUSH_SIZE

        binding.textBrushSize.text = brushSize.toString()
        setupBrushSizeText()

        binding.seekBarBrushSize.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                val value = progress + 1

                binding.textBrushSize.text = value.toString()
                brushSize = value

                onSettingChanged?.invoke(Pair(colorId, brushSize))

                setupBrushSizeText()
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
                // TODO
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
                // TODO
            }

        })
    }

    private fun setupBrushSizeText() {
        val thumb = binding.seekBarBrushSize.thumb
        val thumbOffset = thumb?.bounds?.exactCenterX() ?: 0f
        val thumbX = binding.seekBarBrushSize.x
        val thumbWidth = thumb?.intrinsicWidth ?: 0
        val textX = thumbX + thumbOffset - (binding.textBrushSize.width / 2) + (thumbWidth / 2)
        binding.textBrushSize.x = textX
    }

    companion object {
        private const val MAX_BRUSH_SIZE = 5
    }
}
