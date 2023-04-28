package com.sun.imageeditor.screen.edit

import com.sun.imageeditor.utils.EditType

interface EditFragment {
    val editType: EditType
    val displayName: String
        get() = editType.displayName
}
