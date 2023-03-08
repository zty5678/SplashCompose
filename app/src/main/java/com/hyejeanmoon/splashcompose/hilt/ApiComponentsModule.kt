/*
 * Copyright (C) 2023 HyejeanMOON.
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

package com.hyejeanmoon.splashcompose.hilt

import com.hyejeanmoon.splashcompose.api.SplashOkHttpClient
import com.hyejeanmoon.splashcompose.screen.collections.CollectionsApiService
import com.hyejeanmoon.splashcompose.screen.photos.PhotosApiService
import com.hyejeanmoon.splashcompose.screen.userdetail.UserDetailApiService
import com.hyejeanmoon.splashcompose.EnvParameters
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

@Module
@InstallIn(ViewModelComponent::class)
class ApiComponentsModule {

    @Provides
    fun provideOkhttpClient(): OkHttpClient {
        return SplashOkHttpClient().splashOkHttpClient
    }

    @Provides
    fun provideCollectionsApiService(
        okHttpClient: OkHttpClient
    ): CollectionsApiService {
        return Retrofit
            .Builder()
            .baseUrl(EnvParameters.BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(CollectionsApiService::class.java)
    }

    @Provides
    fun providePhotosApiService(
        okHttpClient: OkHttpClient
    ): PhotosApiService {
        return Retrofit
            .Builder()
            .baseUrl(EnvParameters.BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(PhotosApiService::class.java)
    }

    @Provides
    fun provideUserDetailApiService(
        okHttpClient: OkHttpClient
    ): UserDetailApiService {
        return Retrofit
            .Builder()
            .baseUrl(EnvParameters.BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(UserDetailApiService::class.java)
    }
}