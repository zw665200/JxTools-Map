package com.recovery.tools.callback

interface FileDialogCallback {
    fun onSuccess(str: String)
    fun onCancel()
}