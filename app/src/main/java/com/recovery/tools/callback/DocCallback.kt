package com.recovery.tools.callback

import com.recovery.tools.bean.FileStatus
import com.recovery.tools.bean.FileWithType

interface DocCallback {
    fun onSuccess(step: Enum<FileStatus>)
    fun onProgress(step: Enum<FileStatus>, index: Int)
    fun onProgress(step: Enum<FileStatus>, file: FileWithType)
    fun onFailed(step: Enum<FileStatus>, message: String)
}