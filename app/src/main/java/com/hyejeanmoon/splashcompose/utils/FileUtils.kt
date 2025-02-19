/*
 * Copyright (C) 2021 HyejeanMOON.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.hyejeanmoon.splashcompose.utils

import android.content.ContentValues
import android.content.Context
import android.graphics.Bitmap
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import androidx.annotation.RequiresApi
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.OutputStream
import java.text.SimpleDateFormat
import java.util.*

object FileUtils {
    private const val DAY_IN_MILLIS = 60 * 60 * 24 * 1000

    fun saveImage(context: Context, image: Bitmap) {
        val imageFileName = System.currentTimeMillis().toString() + ".jpg"
        val imageFile = File(context.filesDir, imageFileName)

        try {
            val fOut: OutputStream = FileOutputStream(imageFile)
            image.compress(Bitmap.CompressFormat.JPEG, 100, fOut)
            fOut.close()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    @RequiresApi(Build.VERSION_CODES.Q)
    fun saveImageVersionQ(context: Context, image: Bitmap) {
        val currentTime = System.currentTimeMillis()
        val date = SimpleDateFormat("yyyyMMdd-HHmmss").format(Date(currentTime))
        val fileNameTemplate = "SplashCompose_%s.png"
        val fileName = String.format(fileNameTemplate, date)

        val values = ContentValues()
        values.put(
            MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_PICTURES
                    + File.separator + "SplashCompose"
        )
        values.put(MediaStore.MediaColumns.DISPLAY_NAME, fileName);
        values.put(MediaStore.MediaColumns.MIME_TYPE, "image/png");
        values.put(MediaStore.MediaColumns.DATE_ADDED, currentTime / 1000);
        values.put(MediaStore.MediaColumns.DATE_MODIFIED, currentTime / 1000);
        values.put(
            MediaStore.MediaColumns.DATE_EXPIRES,
            (currentTime + DAY_IN_MILLIS) / 1000
        );
        values.put(MediaStore.MediaColumns.IS_PENDING, 1);

        val resolver = context.contentResolver
        val uri = resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
        try {
            // First, write the actual data for our screenshot
            uri?.also {
                val out = resolver.openOutputStream(it)
                if (out == null){
                    throw IOException()
                }
                if (!image.compress(Bitmap.CompressFormat.PNG, 100, out)) {
                    throw IOException()
                }
                // Everything went well above, publish it!
                values.clear();
                values.put(MediaStore.MediaColumns.IS_PENDING, 0);
                values.putNull(MediaStore.MediaColumns.DATE_EXPIRES);
                resolver.update(uri, values, null, null);
            }
        } catch (exception: Exception) {

        }
    }
}
