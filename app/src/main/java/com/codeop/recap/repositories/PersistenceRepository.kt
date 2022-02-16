package com.codeop.recap.repositories

import android.content.Context
import android.content.SharedPreferences

class PersistenceRepository(context: Context, dbName: String) {

    private val sharedPrefs: SharedPreferences =
        context.getSharedPreferences(dbName, Context.MODE_PRIVATE)

    fun getAllValues(): List<Int> = sharedPrefs.all.mapNotNull { it.value as? Int }

    fun writeInt(key: String, value: Int) = with(sharedPrefs.edit()) {
        putInt(key, value)
        commit()
    }

    fun deleteInt(key: String) = with(sharedPrefs.edit()) {
        remove(key)
        commit()
    }
}