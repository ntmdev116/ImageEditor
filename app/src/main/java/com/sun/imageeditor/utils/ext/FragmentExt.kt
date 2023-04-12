package com.sun.imageeditor.utils.ext

import androidx.fragment.app.Fragment

fun Fragment.addFragment(
    containerId: Int,
    fragment: Fragment,
    isAddBackStack: Boolean = true
) {
    parentFragmentManager.beginTransaction().apply {
        add(containerId, fragment)
        if (isAddBackStack) {
            addToBackStack(null)
        }
        commit()
    }
}
