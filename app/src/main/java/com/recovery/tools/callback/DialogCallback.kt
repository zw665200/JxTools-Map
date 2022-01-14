package com.recovery.tools.callback

import com.recovery.tools.bean.FileBean

interface DialogCallback {
    fun onSuccess(file: FileBean)
    fun onCancel()
}