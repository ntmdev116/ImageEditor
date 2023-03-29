package com.sun.imageeditor.screen.home

import com.sun.imageeditor.databinding.ActivityHomeBinding
import com.sun.imageeditor.utils.base.BaseActivity
import com.sun.imageeditor.utils.ext.*

class HomeActivity : BaseActivity<ActivityHomeBinding>(ActivityHomeBinding::inflate) {

    override fun initView() {
        addFragment(binding.frameHome.id, HomeFragment.newInstance())
    }

    override fun initData() {
    }
}
