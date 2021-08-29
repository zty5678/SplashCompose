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

package com.hyejeanmoon.splashcompose.screen.photos

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import com.hyejeanmoon.splashcompose.api.ApiServiceHelper
import com.hyejeanmoon.splashcompose.api.SplashOkHttpClient
import com.hyejeanmoon.splashcompose.utils.EnvParameters
import com.hyejeanmoon.splashcompose.utils.SharedPreferencesUtils
import dagger.hilt.android.lifecycle.HiltViewModel

@HiltViewModel
class PhotosViewModel(
    app: Application
) : AndroidViewModel(app) {

    private val photosApiService =
        ApiServiceHelper.createPhotosApiService(
            EnvParameters.BASE_URL,
            SplashOkHttpClient().splashOkHttpClient
        )

    private val pref = SharedPreferencesUtils(app)
    val resolution = pref.getString(SharedPreferencesUtils.KEY_DISPLAY_RESOLUTION)
    val orderBy = pref.getString(SharedPreferencesUtils.KEY_ORDER_BY)

    private val photosDataSource =
        PhotosDataSource(PhotosRepository(photosApiService,orderBy))

    var photoList = Pager(
        config = PagingConfig(
            pageSize = PAGE_SIZE,
            enablePlaceholders = false,
            initialLoadSize = INITIAL_LOAD_SIZE
        ),
        pagingSourceFactory = { photosDataSource }
    ).flow.cachedIn(viewModelScope)

    companion object{
        private const val PAGE_SIZE = 10
        private const val INITIAL_LOAD_SIZE = 10
    }
}