package com.recovery.tools.callback

import com.recovery.tools.bean.FileStatus

interface FileCallback {
    fun onSuccess(step: Enum<FileStatus>)
    fun onProgress(step: Enum<FileStatus>, index: Int)
    fun onFailed(step: Enum<FileStatus>, message: String)
}