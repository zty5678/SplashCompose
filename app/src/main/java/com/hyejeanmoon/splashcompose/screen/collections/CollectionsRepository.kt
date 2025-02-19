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

package com.hyejeanmoon.splashcompose.screen.collections

import com.hyejeanmoon.splashcompose.api.ApiEnqueueCallback
import com.hyejeanmoon.splashcompose.entity.Collections
import com.hyejeanmoon.splashcompose.entity.Photo
import javax.inject.Inject
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

class CollectionsRepository @Inject constructor(
    private val collectionsApiService: CollectionsApiService
) {
    suspend fun getCollections(
        page: Int,
        perPage: Int
    ): List<Collections> = suspendCoroutine {
        collectionsApiService.getCollections(
            page,
            perPage
        ).enqueue(
            ApiEnqueueCallback({ response ->
                it.resume(response)
            }, { exception ->
                it.resumeWithException(exception)
            })
        )
    }

    suspend fun getPhotosOfCollection(
        id: String,
        page: Int,
        perPage: Int
    ): List<Photo> = suspendCoroutine {
        collectionsApiService.getPhotosOfCollection(
            id,
            page,
            perPage
        ).enqueue(
            ApiEnqueueCallback({ response ->
                it.resume(response)
            }, { exception ->
                it.resumeWithException(exception)
            })
        )
    }
}