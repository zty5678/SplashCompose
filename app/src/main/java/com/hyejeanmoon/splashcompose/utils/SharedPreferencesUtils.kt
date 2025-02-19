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

import android.content.SharedPreferences

fun SharedPreferences.putString(name: String, data: String) {
    val editor = this.edit()
    editor.putString(name, data)
    editor.apply()
}

fun SharedPreferences.getString(name: String): String {
    return this.getString(name, "") ?: ""
}

fun SharedPreferences.putInt(name: String, position: Int) {
    val editor = this.edit()
    editor.putInt(name, position)
    editor.apply()
}

fun SharedPreferences.getInt(name: String): Int {
    return this.getInt(name, 0)
}
