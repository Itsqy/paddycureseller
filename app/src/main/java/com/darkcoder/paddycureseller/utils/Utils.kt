package com.darkcoder.paddycure.utils

import android.app.Activity
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.ImageView
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.time.LocalDateTime
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter

class Utils {

    fun setupUI(view: View, context: Activity) {

        // Set up touch listener for non-text box views to hide keyboard.
        if (view !is EditText) {
            view.setOnTouchListener(View.OnTouchListener { v, event ->
                hideSoftKeyboard(context)
                false
            })
        }
        //If a layout container, iterate over children and seed recursion.
        if (view is ViewGroup) {
            for (i in 0 until (view as ViewGroup).childCount) {
                val innerView: View = (view as ViewGroup).getChildAt(i)
                setupUI(innerView, context)
            }
        }
    }

    fun hideSoftKeyboard(activity: Activity) {
        val inputMethodManager =
            activity.getSystemService(AppCompatActivity.INPUT_METHOD_SERVICE) as InputMethodManager
        if (inputMethodManager.isAcceptingText) {
            inputMethodManager.hideSoftInputFromWindow(activity.currentFocus!!.windowToken, 0)
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun formatWaktu(input: String): String {
        val utcDateTime = LocalDateTime.parse(input, DateTimeFormatter.ISO_DATE_TIME)
            .atOffset(ZoneOffset.UTC)
        val indonesiaDateTime = utcDateTime.withOffsetSameInstant(ZoneOffset.ofHours(7))
        val formatter = DateTimeFormatter.ofPattern("d MMMM yyyy HH:mm 'WIB'")

        return indonesiaDateTime.format(formatter)
    }
//    fun convertBase64ToImage(base64String: String): Bitmap {
//        val decodedBytes = Base64.decode(base64String, Base64.DEFAULT)
//        return BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.size)
//    }


    fun reduceFileImage(file: File): File {
        val bitmap = BitmapFactory.decodeFile(file.path)
        var compressQuality = 100
        var streamLength: Int
        do {
            val bmpStream = ByteArrayOutputStream()
            bitmap.compress(Bitmap.CompressFormat.JPEG, compressQuality, bmpStream)
            val bmpPicByteArray = bmpStream.toByteArray()
            streamLength = bmpPicByteArray.size
            compressQuality -= 5
        } while (streamLength > 1000000)
        bitmap.compress(Bitmap.CompressFormat.JPEG, compressQuality, FileOutputStream(file))

        return file
    }

}