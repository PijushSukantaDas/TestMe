package com.silverblaze.myapplication.utils

import android.annotation.SuppressLint
import android.content.ContentResolver
import android.net.Uri
import android.provider.OpenableColumns

@SuppressLint("Range")
fun ContentResolver.getFileName(imageUri : Uri): String {
    var name = ""
    val cursor = query(imageUri,null,null,null,null)
    cursor?.use {
        it.moveToFirst()
        name = cursor.getString(it.getColumnIndex(OpenableColumns.DISPLAY_NAME))
    }

    return name
}