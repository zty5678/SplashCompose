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

package com.hyejeanmoon.splashcompose.screen.favorites

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.rememberImagePainter
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import com.hyejeanmoon.splashcompose.R
import com.hyejeanmoon.splashcompose.db.FavoritePhoto
import com.hyejeanmoon.splashcompose.screen.photodetail.PhotoDetailActivity

@ExperimentalFoundationApi
@Composable
fun FavoritesScreen(
    modifier: Modifier = Modifier,
    viewModel: FavoritesViewModel = hiltViewModel()
) {
    val isRefreshing by viewModel.isRefreshing.collectAsState()
    val favoritePhotoList by viewModel.favoritePhotoList.observeAsState()

    SwipeRefresh(
        modifier = Modifier.fillMaxSize(),
        state = rememberSwipeRefreshState(isRefreshing),
        onRefresh = { viewModel.getAllFavoritePhotos() }
    ) {
        favoritePhotoList?.also { photoList ->

            if (photoList.isEmpty()) {
                NoFavoritePhoto()
            } else {
                LazyVerticalGrid(
                    columns = GridCells.Adaptive(160.dp),
                    modifier = modifier
                        .fillMaxSize()
                        .padding(0.dp, 0.dp, 0.dp, 48.dp)
                ) {
                    items(photoList) { photo ->
                        FavoritePhotoItem(
                            photo = photo
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun FavoritePhotoItem(
    modifier: Modifier = Modifier,
    photo: FavoritePhoto
) {
    val context = LocalContext.current

    Image(
        modifier = modifier
            .size(160.dp)
            .clickable {
                PhotoDetailActivity.start(photo.id, context)
            },
        painter = rememberImagePainter(
            photo.photoUrl.orEmpty()
        ),
        contentDescription = "favorite photo",
        contentScale = ContentScale.Crop
    )
}

@Composable
fun NoFavoritePhoto(
    modifier: Modifier = Modifier
) {

    ConstraintLayout(
        modifier = modifier
            .fillMaxSize()
            .padding(0.dp, 50.dp, 0.dp, 0.dp)
    ) {

        val (img, txt) = createRefs()

        Image(
            modifier = Modifier
                .size(128.dp)
                .constrainAs(img) {
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                    top.linkTo(parent.top)
                },
            painter = painterResource(id = R.drawable.ic_none_picture),
            contentDescription = "no favorite photos"
        )

        Text(
            modifier = Modifier
                .padding(0.dp, 40.dp, 0.dp, 0.dp)
                .constrainAs(txt) {
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                    top.linkTo(img.bottom)
                },
            text = "There are not favorite photos!",
            textAlign = TextAlign.Center,
            color = Color.Black,
            fontSize = 16.sp
        )
    }
}