package com.silverblaze.myapplication.utils

import android.content.Context
import dagger.hilt.android.qualifiers.ApplicationContext

class Preference(@ApplicationContext context : Context) {
    private val sharedPreferences = context.getSharedPreferences("key_value",Context.MODE_PRIVATE)
    private val editor = sharedPreferences.edit()

    fun addBoolean(key:String,value:Boolean){
        editor.putBoolean(key,value).apply()
    }
    fun addString(key: String,value: String){
        editor.putString(key,value).apply()
    }

    fun addInt(key :String,value: Int){
        editor.putInt(key,value).apply()
    }

    fun getBoolean(key: String): Boolean {
        return sharedPreferences.getBoolean(key,false)
    }

    fun getString(key: String): String? {
        return sharedPreferences.getString(key,null)
    }

    fun getInt(key:String): Int {
        return sharedPreferences.getInt(key,0)
    }
}