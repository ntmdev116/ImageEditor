package com.sun.imageeditor.data.repository.source

interface OnResultListener<T> {
    fun onSuccess(result: T)
    fun onFail(msg: String)
}
