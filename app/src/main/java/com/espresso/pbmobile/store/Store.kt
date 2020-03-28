package com.espresso.pbmobile.store

import android.content.Context

class Store(context: Context) {
    private val sharedPreferences = context.getSharedPreferences(SETTINGS, Context.MODE_PRIVATE)

    var userType: String
        get() = sharedPreferences.getString(Key.USER_TYPE.name, STRING_DEFAULT_VALUE) ?: STRING_DEFAULT_VALUE
        set(value) = put(Key.USER_TYPE, value)

    var userId: Long
        get() = sharedPreferences.getLong(Key.USER_ID.name, LONG_DEFAULT_VALUE)
        set(value) = put(Key.USER_ID, value)

    private fun put(key: Key, value: Boolean) {
        sharedPreferences.edit().putBoolean(key.name, value).apply()
    }

    private fun put(key: Key, value: String) {
        sharedPreferences.edit().putString(key.name, value)?.apply()
    }

    private fun put(key: Key, value: Int) {
        sharedPreferences.edit().putInt(key.name, value)?.apply()
    }

    private fun put(key: Key, value: Long) {
        sharedPreferences.edit().putLong(key.name, value)?.apply()
    }

    private enum class Key {
        USER_TYPE,
        USER_ID
    }

    companion object {
        const val LONG_DEFAULT_VALUE = -1L
        const val STRING_DEFAULT_VALUE = "default_value"
        private const val SETTINGS = "SETTINGS"
    }
}
