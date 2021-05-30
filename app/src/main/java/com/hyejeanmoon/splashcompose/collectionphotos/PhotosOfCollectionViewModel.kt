package com.hyejeanmoon.splashcompose.collectionphotos

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.paging.Pager
import androidx.paging.PagingConfig
import com.hyejeanmoon.splashcompose.api.ApiServiceHelper
import com.hyejeanmoon.splashcompose.api.OkHttpClient
import com.hyejeanmoon.splashcompose.collections.CollectionsRepository
import com.hyejeanmoon.splashcompose.utils.EnvParameters

class PhotosOfCollectionViewModel(
    private val state: SavedStateHandle
):ViewModel() {

    private var id = ""

    init {
        id = state.get<String>(PhotosOfCollectionActivity.INTENT_ID).orEmpty()
    }

    private val collectionsApiService =
        ApiServiceHelper.createCollectionsApiService(
            EnvParameters.BASE_URL,
            OkHttpClient().splashOkHttpClient
        )

    private val collectionRepository = CollectionsRepository(collectionsApiService)

    private val photosOfCollectionDataSource =
        PhotosOfCollectionDataSource(collectionRepository,id)

    var photosOfCollections = Pager(
        config = PagingConfig(
            pageSize = 10,
            enablePlaceholders = false,
            initialLoadSize = 30
        ),
        pagingSourceFactory = { photosOfCollectionDataSource }
    ).flow
}