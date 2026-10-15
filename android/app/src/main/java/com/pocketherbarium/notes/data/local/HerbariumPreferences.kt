package com.pocketherbarium.notes.data.local

import android.content.Context
import android.content.SharedPreferences

class HerbariumPreferences(context: Context) {

    private val prefs: SharedPreferences =
        context.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE)

    private var readFailed = false

    fun specimens(): String = readString(KEY_SPECIMENS)

    fun writeSpecimens(raw: String) = writeString(KEY_SPECIMENS, raw)

    fun collections(): String = readString(KEY_COLLECTIONS)

    fun writeCollections(raw: String) = writeString(KEY_COLLECTIONS, raw)

    fun activeCollection(): String = readString(KEY_ACTIVE)

    fun writeActiveCollection(name: String) = writeString(KEY_ACTIVE, name)

    fun showLatin(): Boolean = readBoolean(KEY_SHOW_LATIN, true)

    fun writeShowLatin(value: Boolean) = writeBoolean(KEY_SHOW_LATIN, value)

    fun defaultHabitat(): String = readString(KEY_DEFAULT_HABITAT)

    fun writeDefaultHabitat(value: String) = writeString(KEY_DEFAULT_HABITAT, value)

    fun markerOnNew(): Boolean = readBoolean(KEY_MARKER_ON_NEW, true)

    fun writeMarkerOnNew(value: Boolean) = writeBoolean(KEY_MARKER_ON_NEW, value)

    fun seeded(): Boolean = readBoolean(KEY_SEEDED, false)

    fun markSeeded(value: Boolean) = writeBoolean(KEY_SEEDED, value)

    fun lastReadFailed(): Boolean = readFailed

    private fun readString(key: String): String = try {
        prefs.getString(key, "").orEmpty()
    } catch (e: Exception) {
        readFailed = true
        ""
    }

    private fun writeString(key: String, value: String) {
        try {
            prefs.edit().putString(key, value).apply()
        } catch (e: Exception) {
            readFailed = true
        }
    }

    private fun readBoolean(key: String, fallback: Boolean): Boolean = try {
        prefs.getBoolean(key, fallback)
    } catch (e: Exception) {
        readFailed = true
        fallback
    }

    private fun writeBoolean(key: String, value: Boolean) {
        try {
            prefs.edit().putBoolean(key, value).apply()
        } catch (e: Exception) {
            readFailed = true
        }
    }

    companion object {
        private const val FILE_NAME = "pocket_herbarium_prefs"
        private const val KEY_SPECIMENS = "specimens"
        private const val KEY_COLLECTIONS = "collections"
        private const val KEY_ACTIVE = "active_collection"
        private const val KEY_SHOW_LATIN = "show_latin"
        private const val KEY_DEFAULT_HABITAT = "default_habitat"
        private const val KEY_MARKER_ON_NEW = "marker_on_new"
        private const val KEY_SEEDED = "seeded"
    }
}
