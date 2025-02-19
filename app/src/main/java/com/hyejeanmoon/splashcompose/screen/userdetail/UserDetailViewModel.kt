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

package com.hyejeanmoon.splashcompose.screen.userdetail

import android.app.Application
import android.content.SharedPreferences
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.*
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.hyejeanmoon.splashcompose.api.ApiEnqueueCallback
import com.hyejeanmoon.splashcompose.entity.Collections
import com.hyejeanmoon.splashcompose.entity.UserDetail
import com.hyejeanmoon.splashcompose.entity.UsersPhotos
import com.hyejeanmoon.splashcompose.EnvParameters
import com.hyejeanmoon.splashcompose.utils.LogUtils
import com.hyejeanmoon.splashcompose.utils.getString
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@HiltViewModel
class UserDetailViewModel @Inject constructor(
    val app: Application,
    val state: SavedStateHandle,
    private val userDetailPhotosDataSource: UserDetailPhotosDataSource,
    private val userDetailLikedPhotosDataSource: UserDetailLikedPhotosDataSource,
    private val userDetailCollectionsDataSource: UserDetailCollectionsDataSource,
    private val sharedPreferences: SharedPreferences,
    private val userDetailApiService: UserDetailApiService
) : AndroidViewModel(app) {

    private val _userDetail: MutableLiveData<UserDetail> = MutableLiveData()
    val userDetail: LiveData<UserDetail> get() = _userDetail

    private val _exception: MutableLiveData<Exception> = MutableLiveData()
    val exception: LiveData<Exception> get() = _exception

    fun getUserDetailInfo() {
        LogUtils.outputLog("getUserDetailInfo")
        val userName = sharedPreferences.getString(
            EnvParameters.KEY_PHOTO_USER_NAME
        )
        userDetailApiService.getUserDetails(
            userName = userName
        ).enqueue(ApiEnqueueCallback(
            {
                _userDetail.value = it
            }, {
                _exception.value = it
            })
        )
    }

    private val userDetailPhotosFlow by lazy {
        Pager(
            config = PagingConfig(
                pageSize = PAGE_SIZE,
                enablePlaceholders = false,
                initialLoadSize = INITIAL_LOAD_SIZE
            ),
            pagingSourceFactory = { userDetailPhotosDataSource }
        ).flow.cachedIn(viewModelScope)
    }

    private val userDetailCollectionsFlow by lazy {
        Pager(
            config = PagingConfig(
                pageSize = PAGE_SIZE,
                enablePlaceholders = false,
                initialLoadSize = INITIAL_LOAD_SIZE
            ),
            pagingSourceFactory = { userDetailCollectionsDataSource }
        ).flow.cachedIn(viewModelScope)
    }

    private val userDetailLikedPhotosDataSourceFlow by lazy {
        Pager(
            config = PagingConfig(
                pageSize = PAGE_SIZE,
                enablePlaceholders = false,
                initialLoadSize = INITIAL_LOAD_SIZE
            ),
            pagingSourceFactory = { userDetailLikedPhotosDataSource }
        ).flow.cachedIn(viewModelScope)
    }

    var viewState by mutableStateOf(
        UserDetailViewState(
            pagingPhotos = userDetailPhotosFlow,
            pagingCollections = userDetailCollectionsFlow,
            pagingLikedPhotos = userDetailLikedPhotosDataSourceFlow
        )
    )

    companion object {
        private const val PAGE_SIZE = 15
        private const val INITIAL_LOAD_SIZE = 15
    }
}

data class UserDetailViewState(
    val pagingPhotos: PagingPhotos,
    val pagingCollections: PagingCollections,
    val pagingLikedPhotos: PagingLikedPhotos
)

typealias PagingPhotos = Flow<PagingData<UsersPhotos>>
typealias PagingCollections = Flow<PagingData<Collections>>
typealias PagingLikedPhotos = Flow<PagingData<UsersPhotos>>