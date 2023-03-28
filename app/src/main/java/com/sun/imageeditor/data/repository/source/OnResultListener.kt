package com.sun.imageeditor.data.repository.source

interface OnResultListener<T> {
    fun onSuccess(list: T)
    fun onFail(msg: String)
}
