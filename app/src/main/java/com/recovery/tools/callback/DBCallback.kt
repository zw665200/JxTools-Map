package com.recovery.tools.callback

import com.recovery.tools.bean.FileStatus

interface DBCallback {
    fun onSuccess(step: Enum<FileStatus>)
    fun onProgress(step: Enum<FileStatus>, message: String)
    fun onProgress(step: Enum<FileStatus>, index: Int)
    fun onFailed(step: Enum<FileStatus>, message: String)
}