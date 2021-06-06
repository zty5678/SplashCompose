package com.hyejeanmoon.splashcompose.screen.collectionphotos

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.items
import com.hyejeanmoon.splashcompose.entity.Photo
import com.hyejeanmoon.splashcompose.screen.photos.PhotoImage

@Composable
fun PhotosOfCollectionScreen(
    modifier: Modifier = Modifier,
    photosOfCollectionViewModel: PhotosOfCollectionViewModel,
    onPhotoClick: (Photo?) -> Unit
) {
    val pagingItems = photosOfCollectionViewModel.photosOfCollections.collectAsLazyPagingItems()

    LazyColumn(
        modifier = modifier.fillMaxSize()
    ) {
        items(lazyPagingItems = pagingItems) { photoItem ->
            val item by remember {
                mutableStateOf(photoItem)
            }
            PhotoImage(photo = item, onPhotoClick = onPhotoClick)
        }
    }
}