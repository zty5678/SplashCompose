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

package com.hyejeanmoon.splashcompose

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.selection.selectable
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.hyejeanmoon.splashcompose.db.AppDatabase
import com.hyejeanmoon.splashcompose.screen.collectionphotos.PhotosOfCollectionActivity
import com.hyejeanmoon.splashcompose.screen.collections.CollectionsScreen
import com.hyejeanmoon.splashcompose.screen.collections.CollectionsViewModel
import com.hyejeanmoon.splashcompose.screen.favorites.FavoritesActivity
import com.hyejeanmoon.splashcompose.screen.photodetail.PhotoDetailActivity
import com.hyejeanmoon.splashcompose.screen.photos.PhotoScreen
import com.hyejeanmoon.splashcompose.screen.photos.PhotosViewModel
import com.hyejeanmoon.splashcompose.screen.random.RandomPhotoScreen
import com.hyejeanmoon.splashcompose.screen.random.RandomPhotoViewModel
import com.hyejeanmoon.splashcompose.screen.settings.SettingItemDetail
import com.hyejeanmoon.splashcompose.screen.settings.SettingsItem
import com.hyejeanmoon.splashcompose.screen.settings.SettingsScreen
import com.hyejeanmoon.splashcompose.screen.settings.SettingsViewModel
import com.hyejeanmoon.splashcompose.screen.userdetail.UserDetailActivity
import com.hyejeanmoon.splashcompose.screen.webview.WebViewActivity
import com.hyejeanmoon.splashcompose.ui.theme.SplashComposeTheme
import com.hyejeanmoon.splashcompose.utils.DataManager
import com.hyejeanmoon.splashcompose.utils.SharedPreferencesUtils
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val photosViewModel: PhotosViewModel by viewModels()
    private val collectionsViewModel: CollectionsViewModel by viewModels()
    private val settingsViewModel: SettingsViewModel by viewModels()
    private val randomPhotoViewModel: RandomPhotoViewModel by viewModels()

    @ExperimentalFoundationApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initPref()

        randomPhotoViewModel.getRandomPhoto()

        setContent {

            var expanded by remember { mutableStateOf(false) }

            SetUpStatusBar()

            SplashComposeTheme {
                Surface(color = MaterialTheme.colors.background) {
                    val navController = rememberNavController()
                    Scaffold(
                        modifier = Modifier,
                        bottomBar = {
                            BottomNavigation {
                                val screenList = listOf(
                                    Screen.Random,
                                    Screen.Photos,
                                    Screen.Collections,
                                    Screen.Settings,
                                )

                                val navBackStackEntry by navController.currentBackStackEntryAsState()
                                val currentRoute = navBackStackEntry?.destination?.route

                                screenList.forEach { screen ->
                                    BottomNavigationItem(
                                        selected = currentRoute == screen.route,
                                        onClick = {
                                            navController.navigate(screen.route) {
                                                launchSingleTop = true
                                            }
                                        },
                                        icon = {
                                            Icon(
                                                modifier = Modifier.size(28.dp),
                                                painter = painterResource(id = screen.drawableId),
                                                contentDescription = stringResource(
                                                    id = screen.stringId
                                                ),
                                                tint = if (currentRoute == screen.route) Color.Red else Color.Black
                                            )
                                        },
                                        label = {
                                            Text(
                                                text = stringResource(id = screen.stringId),
                                                color = if (currentRoute == screen.route) Color.Red else Color.Black
                                            )
                                        }
                                    )
                                }
                            }
                        },
                        topBar = {
                            TopAppBar(
                                title = {
                                    Text(
                                        text = APPLICATION_TITLE,
                                        color = Color.Black
                                    )
                                },
                                actions = {
                                    IconButton(
                                        onClick = {
                                            expanded = true
                                        },
                                        content = {
                                            Icon(
                                                imageVector = Icons.Filled.MoreVert,
                                                contentDescription = "More Icon",
                                                tint = Color.Black
                                            )
                                        }
                                    )
                                    DropdownMenu(
                                        expanded = expanded,
                                        onDismissRequest = { expanded = false },
                                        content = {
                                            Column {
                                                DropdownMenuItem(onClick = {
                                                    expanded = false
                                                    FavoritesActivity.start(this@MainActivity)
                                                }) {
                                                    Text(text = getString(R.string.title_favorites))
                                                }
                                            }
                                        }
                                    )
                                }
                            )
                        }
                    ) {

                        var openDialog = remember { mutableStateOf(false) }
                        var settingsItem = remember { mutableStateOf("") }

                        NavHost(navController, startDestination = Screen.Random.route) {
                            composable(Screen.Random.route) {
                                RandomPhotoScreen(
                                    viewModel = randomPhotoViewModel,
                                    onRandomPhotoClick = {
                                        PhotoDetailActivity.start(it, this@MainActivity)
                                    },
                                    onUserInfoClick = { userName ->
                                        UserDetailActivity.startUserDetailActivity(
                                            this@MainActivity,
                                            userName
                                        )
                                    }
                                )
                            }
                            composable(Screen.Photos.route) {
                                PhotoScreen(
                                    viewModel = photosViewModel,
                                    onPhotoClick = {
                                        PhotoDetailActivity.start(
                                            it?.id.orEmpty(),
                                            this@MainActivity
                                        )
                                    },
                                    onUserInfoClick = { userName ->
                                        UserDetailActivity.startUserDetailActivity(
                                            this@MainActivity,
                                            userName
                                        )
                                    }
                                )
                            }
                            composable(Screen.Collections.route) {
                                CollectionsScreen(
                                    collectionsViewModel = collectionsViewModel,
                                    onCollectionsItemClick = { collectionId, collectionTitle ->
                                        PhotosOfCollectionActivity.start(
                                            this@MainActivity,
                                            collectionId,
                                            collectionTitle
                                        )
                                    },
                                    onUserInfoClick = { userName ->
                                        UserDetailActivity.startUserDetailActivity(
                                            this@MainActivity,
                                            userName
                                        )
                                    }
                                )
                            }
                            composable(Screen.Settings.route) {
                                SettingsScreen(
                                    settingsItems = listOf(
                                        SettingsItem(
                                            SETTINGS_TITLE_APPLICATION_SETTINGS,
                                            listOf(
                                                SettingItemDetail(
                                                    title = SETTINGS_ITEM_DISPLAY_RESOLUTION
                                                ),
                                                SettingItemDetail(
                                                    title = SETTINGS_ITEM_PHOTO_DISPLAY_ORDER,
                                                    "This setting will take effect the next time you start the app"
                                                ),
                                                SettingItemDetail(title = SETTINGS_ITEM_CLEAR_CACHE)
                                            )
                                        ),
                                        SettingsItem(
                                            SETTINGS_TITLE_OTHERS,
                                            listOf(
                                                SettingItemDetail(
                                                    title = SETTINGS_ITEM_VERSION,
                                                    content = BuildConfig.VERSION_NAME
                                                ),
                                                SettingItemDetail(
                                                    title = SETTINGS_ITEM_LICENSES
                                                ),
                                                SettingItemDetail(title = SETTINGS_ITEM_ABOUT_DEVELOPER)
                                            )
                                        )
                                    ),
                                    onSettingsItemClick = { title ->
                                        when (title) {
                                            SETTINGS_ITEM_DISPLAY_RESOLUTION,
                                            SETTINGS_ITEM_PHOTO_DISPLAY_ORDER -> {
                                                openDialog.value = true
                                                settingsItem.value = title
                                            }
                                            SETTINGS_ITEM_ABOUT_DEVELOPER,
                                            SETTINGS_ITEM_VERSION,
                                            SETTINGS_ITEM_CLEAR_CACHE,
                                            SETTINGS_ITEM_LICENSES -> {
                                                when (title) {
                                                    SETTINGS_ITEM_ABOUT_DEVELOPER -> {
                                                        val uri =
                                                            Uri.parse("https://github.com/HyejeanMOON")
                                                        val intent = Intent(Intent.ACTION_VIEW, uri)
                                                        startActivity(intent)
                                                    }

                                                    SETTINGS_ITEM_VERSION -> {
                                                        // do nothing
                                                    }

                                                    SETTINGS_ITEM_LICENSES -> {
                                                        WebViewActivity.start(
                                                            "file:///android_asset/licenses.html",
                                                            SETTINGS_ITEM_LICENSES,
                                                            this@MainActivity
                                                        )
                                                    }

                                                    SETTINGS_ITEM_CLEAR_CACHE -> {
                                                        DataManager.clearCacheInScopedStorage(this@MainActivity)
                                                    }
                                                }
                                            }
                                        }
                                    }
                                )
                            }
                        }
                        if (openDialog.value) {
                            SettingsAlertDialog(
                                item = settingsItem.value,
                                onCloseDialog = {
                                    openDialog.value = false
                                },
                                settingsViewModel = settingsViewModel
                            )
                        }
                    }
                }
            }

        }
    }

    private fun initPref() {
        val pref = SharedPreferencesUtils(this)

        //  display resolution
        val displayResolution = pref.getString(SharedPreferencesUtils.KEY_DISPLAY_RESOLUTION)
        if (displayResolution.isBlank()) {
            pref.putString(
                SharedPreferencesUtils.KEY_DISPLAY_RESOLUTION,
                Resolution.REGULAR.name
            )
        }
        //  order by
        val orderBy = pref.getString(SharedPreferencesUtils.KEY_ORDER_BY)
        if (orderBy.isBlank()) {
            pref.putString(
                SharedPreferencesUtils.KEY_ORDER_BY,
                OrderBy.LATEST.name
            )
        }
    }

    override fun onBackPressed() {
        // inorder to avoid memory leaks
        AppDatabase.destroyInstance()
        finish()
    }

    companion object {
        const val SETTINGS_ITEM_ABOUT_DEVELOPER = "About Developer"
        const val SETTINGS_ITEM_VERSION = "Version"
        const val SETTINGS_ITEM_CLEAR_CACHE = "Clear Cache"
        const val SETTINGS_ITEM_PHOTO_DISPLAY_ORDER = "Photo Display Order"
        const val SETTINGS_ITEM_DISPLAY_RESOLUTION = "Display Resolution"
        const val SETTINGS_ITEM_LICENSES = "Licenses"
        const val SETTINGS_TITLE_OTHERS = "Others"
        const val SETTINGS_TITLE_APPLICATION_SETTINGS = "Application Settings"
        const val APPLICATION_TITLE = "Moonlight Pictures"
    }
}

@Composable
fun SettingsAlertDialog(
    modifier: Modifier = Modifier,
    settingsViewModel: SettingsViewModel,
    item: String,
    onCloseDialog: () -> Unit
) {
    AlertDialog(
        modifier = modifier.padding(40.dp, 0.dp),
        onDismissRequest = {
            onCloseDialog()
        },
        confirmButton = {
            Button(
                onClick = {
                    onCloseDialog()
                }
            ) {
                Text(
                    text = "Dismiss",
                    color = Color.Black,
                )
            }
        },
        title = {
            Text(
                text = item,
                color = Color.Black
            )
        },
        text = {
            RadioButtonList(
                settingsViewModel = settingsViewModel,
                item = item
            )
        }
    )
}

@Composable
fun RadioButtonList(
    modifier: Modifier = Modifier,
    settingsViewModel: SettingsViewModel,
    item: String
) {
    var radioOptionList: List<String> = listOf()
    var initialPosition = 0

    when (item) {
        MainActivity.SETTINGS_ITEM_PHOTO_DISPLAY_ORDER -> {
            radioOptionList = settingsViewModel.orderByList
            initialPosition = settingsViewModel.getOrderByPosition()
        }
        MainActivity.SETTINGS_ITEM_DISPLAY_RESOLUTION -> {
            radioOptionList = settingsViewModel.displayResolutionList
            initialPosition = settingsViewModel.getDisplayResolutionPosition()
        }
    }

    var selectedOption = remember { mutableStateOf(initialPosition) }
    Column(
        modifier = modifier
    ) {
        radioOptionList.forEachIndexed { index, text ->
            Row(
                Modifier
                    .selectable(
                        selected = (text == radioOptionList[selectedOption.value]),
                        onClick = {
                            selectedOption.value = index
                            when (item) {
                                MainActivity.SETTINGS_ITEM_PHOTO_DISPLAY_ORDER -> {
                                    settingsViewModel.putOrderBy(text)
                                }
                                MainActivity.SETTINGS_ITEM_DISPLAY_RESOLUTION -> {
                                    settingsViewModel.putDisplayResolution(text)
                                }
                            }
                        }
                    )
                    .padding(horizontal = 20.dp)
            ) {
                RadioButton(
                    selected = (text == radioOptionList[selectedOption.value]),
                    onClick = {
                        selectedOption.value = index
                        when (item) {
                            MainActivity.SETTINGS_ITEM_PHOTO_DISPLAY_ORDER -> {
                                settingsViewModel.putOrderBy(text)
                            }
                            MainActivity.SETTINGS_ITEM_DISPLAY_RESOLUTION -> {
                                settingsViewModel.putDisplayResolution(text)
                            }
                        }
                    }
                )
                Text(
                    text = text,
                    style = MaterialTheme.typography.body1.merge(),
                    modifier = Modifier.padding(start = 16.dp)
                )
            }
        }
    }
}