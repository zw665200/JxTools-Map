package com.recovery.tools.callback

interface HttpCallback {
    fun onSuccess()
    fun onFailed(msg: String)
}