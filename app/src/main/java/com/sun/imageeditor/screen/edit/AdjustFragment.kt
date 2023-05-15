package com.sun.imageeditor.screen.edit

import android.widget.SeekBar
import com.sun.imageeditor.databinding.FragmentAdjustBinding
import com.sun.imageeditor.screen.edit.adapter.AdjustAdapter
import com.sun.imageeditor.utils.AdjustType
import com.sun.imageeditor.utils.Constant
import com.sun.imageeditor.utils.EditParameters
import com.sun.imageeditor.utils.EditType
import com.sun.imageeditor.utils.OnItemRecyclerViewClickListener

import com.sun.imageeditor.utils.base.BaseFragment

class AdjustFragment : BaseFragment<FragmentAdjustBinding>(
    FragmentAdjustBinding::inflate
), EditFragment {

    private var mCurrentAdjustType: AdjustType = AdjustType.BRIGHTNESS
    private val mAdjustTypeToProgressMap by lazy {
        mutableMapOf(
            AdjustType.BRIGHTNESS to Constant.ADJUST_MAX_VALUE_TO_DISPLAY,
            AdjustType.CONTRAST to Constant.ADJUST_MAX_VALUE_TO_DISPLAY,
        )
    }
    private val mPresenter by lazy { EditPresenter.getInstance() }

    override val editType: EditType
        get() = EditType.ADJUST

    override fun initData() {
        // TODO
    }

    override fun initView() {
        binding.seekbar.max =
            Constant.ADJUST_MAX_VALUE_TO_DISPLAY + Constant.ADJUST_MAX_VALUE_TO_DISPLAY
        binding.seekbar.progress =
            Constant.ADJUST_MAX_VALUE_TO_DISPLAY

        binding.seekbar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                val standardizedProgress = progressToShow(progress)

                binding.textAdjustProgress.text = standardizedProgress.toString()

                if (fromUser) {
                    mAdjustTypeToProgressMap[mCurrentAdjustType] = progress

                    mPresenter.onEditButtonClick(
                        editType,
                        EditParameters(
                            brightness = mAdjustTypeToProgressMap[AdjustType.BRIGHTNESS]?.let {
                                standardizeProgress(it)
                            },
                            contrast = mAdjustTypeToProgressMap[AdjustType.CONTRAST]?.let {
                                standardizeProgress(it)
                            },
                        )
                    )
                }
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
                // TODO
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
                // TODO
            }

        })

        binding.textAdjustProgress.text = Constant.ADJUST_MAX_VALUE_TO_DISPLAY.toString()

        binding.recyclerViewFilterChooser.adapter = AdjustAdapter().apply {
            setAdjustList(mAdjustTypeToProgressMap.keys.toList())

            setOnItemClickListener(object : OnItemRecyclerViewClickListener<AdjustType> {
                override fun onItemClick(parameter: AdjustType?) {
                    if (parameter != null) {
                        mAdjustTypeToProgressMap[parameter]?.let {
                            mCurrentAdjustType = parameter
                            binding.seekbar.progress = it
                        }
                    }
                }
            })
        }
    }

    private fun progressToShow(progress: Int) =
        progress - Constant.ADJUST_MAX_VALUE_TO_DISPLAY

    private fun standardizeProgress(progress: Int) =
        progressToShow(progress).div(Constant.ADJUST_MAX_VALUE_TO_DISPLAY.toFloat())
}
