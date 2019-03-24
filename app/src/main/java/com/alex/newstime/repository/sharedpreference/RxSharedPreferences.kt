package com.alex.newstime.repository.sharedpreference

import android.content.Context
import android.content.SharedPreferences
import io.reactivex.Single

object RxSharedPreferences {

    private lateinit var preferences: SharedPreferences

    // ----------------------------------------------------------------------------

    fun initialize(context: Context) {
        preferences = context.getSharedPreferences("NewsTimeSharedPreferences", Context.MODE_PRIVATE)
    }

    // ----------------------------------------------------------------------------

    fun putString(key: String, value: String): Single<Boolean> = Single.fromCallable { preferences.edit().putString(key, value).commit() }
    fun getString(key: String): Single<String> = Single.just(preferences.getString(key, ""))
    
    // ----------------------------------------------------------------------------

    fun putInt(key: String, value: Int): Single<Boolean> = Single.fromCallable { preferences.edit().putInt(key, value).commit() }
    fun getInt(key: String, default: Int): Single<Int> = Single.just(preferences.getInt(key, default))
    
    // ----------------------------------------------------------------------------

    fun putLong(key: String, value: Long): Single<Boolean> = Single.fromCallable { preferences.edit().putLong(key, value).commit() }
    fun getLong(key: String, default: Long): Single<Long> = Single.just(preferences.getLong(key, default))
    
    // ----------------------------------------------------------------------------

    fun putBoolean(key: String, value: Boolean): Single<Boolean> = Single.fromCallable { preferences.edit().putBoolean(key, value).commit() }
    fun getBoolean(key: String, default: Boolean): Single<Boolean> = Single.just(preferences.getBoolean(key, default))
    
    // ----------------------------------------------------------------------------

    fun delete(key: String): Single<Boolean> = Single.fromCallable { preferences.edit().remove(key).commit() }
}