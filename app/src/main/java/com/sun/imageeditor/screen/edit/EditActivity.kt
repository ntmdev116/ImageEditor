package com.sun.imageeditor.screen.edit

import androidx.fragment.app.Fragment
import com.google.android.material.tabs.TabLayoutMediator
import com.sun.imageeditor.R
import com.sun.imageeditor.databinding.ActivityEditBinding
import com.sun.imageeditor.screen.search.adapter.SearchViewPagerAdapter
import com.sun.imageeditor.utils.base.BaseActivity
import com.sun.imageeditor.utils.ext.loadOriginalImageWithUrl

class EditActivity : BaseActivity<ActivityEditBinding>(ActivityEditBinding::inflate) {
    override fun initView() {
        setupTabLayout()

        val url = intent.getStringExtra(IMAGE_URL)
        url?.let {
            binding.imageMain.loadOriginalImageWithUrl(it, R.color.black, R.drawable.ic_error)
        }
    }

    private fun setupTabLayout() {
        val list = mutableListOf<Fragment>(
            FilterFragment(),
        )
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
        // TODO not yet implemented
    }

    companion object {
        const val IMAGE_URL = "IMAGE_URL"
    }
}
