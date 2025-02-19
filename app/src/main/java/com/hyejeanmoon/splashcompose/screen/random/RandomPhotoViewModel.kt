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

package com.hyejeanmoon.splashcompose.screen.random

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.hyejeanmoon.splashcompose.entity.Photo
import com.hyejeanmoon.splashcompose.screen.photos.PhotosRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RandomPhotoViewModel @Inject constructor(
    val app: Application,
    private val photosRepository: PhotosRepository
) : AndroidViewModel(app) {

    private val _randomPhoto: MutableLiveData<Photo> = MutableLiveData()
    val randomPhoto: LiveData<Photo> get() = _randomPhoto

    private val _exception: MutableLiveData<Exception> = MutableLiveData()
    val exception: LiveData<Exception> get() = _exception

    fun getRandomPhoto() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                _randomPhoto.postValue(
                    photosRepository.getRandomPhoto()
                )
            } catch (exception: Exception) {
                _exception.postValue(exception)
            }
        }
    }
}