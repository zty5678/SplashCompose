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

package com.hyejeanmoon.splashcompose.screen.photodetail

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import com.hyejeanmoon.splashcompose.ErrorAlert
import com.hyejeanmoon.splashcompose.R
import com.hyejeanmoon.splashcompose.SetUpStatusBar
import com.hyejeanmoon.splashcompose.screen.userdetail.UserDetailActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PhotoDetailActivity : ComponentActivity() {

    private val viewModel: PhotoDetailViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel.getPhotoById()
        viewModel.isFavoritePhoto()

        setContent {
            SetUpStatusBar()

            PhotoDetailScreen(
                viewModel = viewModel,
                onBackIconClick = {
                    finish()
                },
                onUserInfoClick = { userName ->
                    UserDetailActivity.startUserDetailActivity(this, userName = userName)
                }
            )
        }
        observeLiveData()
    }

    private fun observeLiveData() {
        viewModel.downloadStart.observe(
            this,
            Observer {
                Toast.makeText(this, getString(R.string.toast_start_download), Toast.LENGTH_LONG)
                    .show()
            }
        )

        viewModel.downloadComplete.observe(
            this,
            Observer {
                Toast.makeText(this, getString(R.string.toast_complete_download), Toast.LENGTH_LONG)
                    .show()
            }
        )
    }

    companion object {
        const val INTENT_PHOTO_ID = "INTENT_PHOTO_ID"

        fun start(photoId: String, activity: ComponentActivity) {
            val intent = Intent()
            intent.setClass(activity, PhotoDetailActivity::class.java)
            intent.putExtra(INTENT_PHOTO_ID, photoId)
            activity.startActivity(intent)
        }
    }
}