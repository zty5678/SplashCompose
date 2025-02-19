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

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.hyejeanmoon.splashcompose.ui.theme.MoonGray

@Composable
fun SetUpStatusBar() {
    // Remember a SystemUiController
    val systemUiController = rememberSystemUiController()
    val useDarkIcons = MaterialTheme.colors.isLight

    SideEffect {
        // Update all of the system bar colors to be transparent, and use
        // dark icons if we're in light theme
        systemUiController.setStatusBarColor(
            color = MoonGray,
            darkIcons = useDarkIcons
        )
    }
}

@Composable
fun ErrorAlert(
    showAlert: Boolean = true
) {
    var openAlert by remember {
        mutableStateOf(showAlert)
    }
    if (openAlert) {
        AlertDialog(
            modifier = Modifier.padding(40.dp, 0.dp),
            onDismissRequest = { },
            confirmButton = {
                Text(
                    modifier = Modifier.clickable { openAlert = false },
                    text = stringResource(id = R.string.alert_error_button)
                )
            },
            title = {
                Text(
                    modifier = Modifier.padding(10.dp, 5.dp),
                    text = stringResource(id = R.string.alert_error_title)
                )
            },
            text = {
                Text(
                    modifier = Modifier.padding(10.dp, 5.dp),
                    text = stringResource(id = R.string.alert_error_message)
                )
            }
        )
    }
}

@Composable
fun ExitAlertDialog(
    viewModel: MainViewModel = hiltViewModel(),
    onConfirmButtonClicked: () -> Unit
) {
    val openAlert by viewModel.shouldShowExitAlertDialog.observeAsState()
    if (openAlert == true) {
        AlertDialog(
            modifier = Modifier.padding(40.dp, 0.dp),
            onDismissRequest = { },
            confirmButton = {
                Text(
                    modifier = Modifier
                        .padding(10.dp, 5.dp)
                        .clickable {
                            viewModel.shouldShowExitAlertDialog.postValue(false)
                            onConfirmButtonClicked()
                        },
                    text = stringResource(id = R.string.alert_exit_ok_button)
                )
            },
            dismissButton = {
                Text(
                    modifier = Modifier
                        .padding(10.dp, 5.dp)
                        .clickable {
                            viewModel.shouldShowExitAlertDialog.postValue(false)
                        },
                    text = stringResource(id = R.string.alert_exit_cancel_button)
                )
            },
            title = { Text(text = stringResource(id = R.string.alert_exit_title)) },
            text = { Text(text = stringResource(id = R.string.alert_exit_message)) }
        )
    }
}