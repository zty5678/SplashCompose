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
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.paging.Pager
import androidx.paging.PagingConfig
import com.hyejeanmoon.splashcompose.api.ApiEnqueueCallback
import com.hyejeanmoon.splashcompose.api.ApiServiceHelper
import com.hyejeanmoon.splashcompose.api.SplashOkHttpClient
import com.hyejeanmoon.splashcompose.entity.UserDetail
import com.hyejeanmoon.splashcompose.utils.EnvParameters
import com.hyejeanmoon.splashcompose.utils.LogUtils
import com.hyejeanmoon.splashcompose.utils.SharedPreferencesUtils
import dagger.assisted.Assisted
import dagger.hilt.android.lifecycle.HiltViewModel

@HiltViewModel
class UserDetailViewModel(
    val app: Application,
    @Assisted val state: SavedStateHandle
) : AndroidViewModel(app) {

    private var userName: String = ""

    init {
        userName = state.get<String>(UserDetailActivity.INTENT_USER_NAME).orEmpty()
    }

    private val pref = SharedPreferencesUtils(app)
    private val orderBy = pref.getString(SharedPreferencesUtils.KEY_ORDER_BY)
    val resolution = pref.getString(SharedPreferencesUtils.KEY_DISPLAY_RESOLUTION)

    private val _userDetail: MutableLiveData<UserDetail> = MutableLiveData()
    val userDetail: LiveData<UserDetail> get() = _userDetail

    private val userDetailApiService = ApiServiceHelper.createUserDetailApiService(
        EnvParameters.BASE_URL,
        SplashOkHttpClient().splashOkHttpClient
    )

    private val _exception: MutableLiveData<Exception> = MutableLiveData()
    val exception: LiveData<Exception> get() = _exception

    private val userDetailRepository = UserDetailRepository(userDetailApiService)
    private val userDetailPhotosDataSource = UserDetailPhotosDataSource(
        userDetailRepository,
        userName
    )
    private val userDetailLikedPhotosDataSource = UserDetailLikedPhotosDataSource(
        userDetailRepository,
        userName,
        orderBy
    )
    private val userDetailCollectionsDataSource = UserDetailCollectionsDataSource(
        userDetailRepository,
        userName,
        orderBy
    )

    fun getUserDetailInfo() {
        LogUtils.outputLog("getUserDetailInfo")
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

    val userDetailPhotosFlow = Pager(
        config = PagingConfig(
            pageSize = PAGE_SIZE,
            enablePlaceholders = false,
            initialLoadSize = INITIAL_LOAD_SIZE
        ),
        pagingSourceFactory = { userDetailPhotosDataSource }
    ).flow

    val userDetailCollectionsFlow = Pager(
        config = PagingConfig(
            pageSize = PAGE_SIZE,
            enablePlaceholders = false,
            initialLoadSize = INITIAL_LOAD_SIZE
        ),
        pagingSourceFactory = { userDetailCollectionsDataSource }
    ).flow

    val userDetailLikedPhotosDataSourceFlow = Pager(
        config = PagingConfig(
            pageSize = PAGE_SIZE,
            enablePlaceholders = false,
            initialLoadSize = INITIAL_LOAD_SIZE
        ),
        pagingSourceFactory = { userDetailLikedPhotosDataSource }
    ).flow

    companion object {
        private const val PAGE_SIZE = 5
        private const val INITIAL_LOAD_SIZE = 5
    }
}