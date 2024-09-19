package com.balajiprabhu.common.utils

import android.content.Context
import androidx.annotation.StringRes

sealed class UiText {

    data class RemoteString(val value: String) : UiText()

    class LocalString(@StringRes val resId: Int, vararg val args: Any) : UiText()

    data object Idle : UiText()

    fun getString(context: Context) : String {
        return when (this) {
            is RemoteString -> value
            is LocalString -> context.getString(resId, *args)
            Idle -> ""
        }
    }
}