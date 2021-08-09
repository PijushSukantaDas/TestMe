package com.silverblaze.myapplication.utils

import android.annotation.SuppressLint
import android.app.Activity
import android.content.ContentResolver
import android.net.Uri
import android.provider.OpenableColumns
import androidx.fragment.app.Fragment
import java.text.SimpleDateFormat
import java.util.*

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

fun Activity.formatDateToString(date : Date): String {
    val format = "dd/MM/yy hh:mm aaa"
    val simpleDateFormat = SimpleDateFormat(format)

    return  simpleDateFormat.format(date)
}

fun Activity.formatStringToDate(date : String): Date {
    val format = "dd/MM/yy hh:mm aaa"
    val simpleDateFormat = SimpleDateFormat(format)
    val dateString = simpleDateFormat.format(date)
    val date = simpleDateFormat.parse(date)

    return  date
}