package com.espresso.pbmobile.store

import android.content.Context
import android.content.SharedPreferences

class Store(private val context: Context) {
    private var sharedPreferences: SharedPreferences? = null

    fun putBoolean(key: String, value: Boolean) = sharedPreferences?.edit()?.putBoolean(key, value)
    fun putString(key: String, value: String) = sharedPreferences?.edit()?.putString(key, value)
    fun putInt(key: String, value: Int) = sharedPreferences?.edit()?.putInt(key, value)

    private enum class KEYS {
        USER_TYPE
    }

    init {
        sharedPreferences = context.getSharedPreferences("SETTINGS", Context.MODE_PRIVATE)
    }
}
