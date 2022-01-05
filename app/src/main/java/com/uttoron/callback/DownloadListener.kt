package com.uttoron.callback

interface DownloadListener {
    fun onSuccess(path: String)
    fun onFailure(error: String)
}