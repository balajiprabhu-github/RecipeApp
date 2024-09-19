package com.balajiprabhu.common.utils

sealed class NetworkResult<T>(
    val data: T? = null,
    val error: String? = null
) {
    class Success<T>(data: T) : NetworkResult<T>(data)
    class Error<T>(error: String?, data: T? = null) : NetworkResult<T>(error = error, data = data)
    class Loading<T> : NetworkResult<T>()
}