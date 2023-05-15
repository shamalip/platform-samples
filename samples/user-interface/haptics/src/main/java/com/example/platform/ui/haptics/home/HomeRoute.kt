/*
 * Copyright (C) 2022 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.platform.ui.haptics.home

import android.annotation.SuppressLint
import android.os.VibrationEffect
import android.view.View
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun HomeRoute(
    viewModel: HomeViewModel,
    onShowMessage: (String) -> Unit,
) {
    HomeScreen(
        homeUiState = viewModel.homeUiState,
        onButtonClicked = viewModel::onButtonClicked,
        onShowMessage = onShowMessage,
    )
}

@Composable
private fun HomeScreen(
    homeUiState: HomeUiState,
    onButtonClicked: (view: View, hapticCategory: HapticCategoryType, hapticId: Int) -> Unit,
    onShowMessage: (message: String) -> Unit,
) {
    Column(
        modifier = Modifier
            .padding(horizontal = 16.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.Start,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        for (category in homeUiState.hapticCategories) {
            HomeHapticCategory(label = category.label) {
                // 2 buttons for ever row for each haptic feedback category.
                for (buttons in category.buttons.chunked(2)) {
                    Row(
                        Modifier
                            .padding(bottom = 16.dp)
                            .fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                    ) {
                        for (button in buttons) {
                            HomeHapticButton(
                                label = button.label,
                                isAvailable = button.worksOnUserDevice,
                                onClick = { view ->
                                    if (!button.worksOnUserDevice) {
                                        onShowMessage("${button.label} is not supported on this device.")
                                        return@HomeHapticButton
                                    }
                                    onButtonClicked(view, category.categoryType, button.hapticId)
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun HomeHapticCategory(
    label: String,
    content: @Composable () -> Unit
) {
    Column {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp, bottom = 16.dp)
        ) {
            Text(
                label,
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.primary
            )
        }
        content()
    }
}

@Composable
private fun HomeHapticButton(
    label: String,
    onClick: (view: View) -> Unit,
    modifier: Modifier = Modifier,
    isAvailable: Boolean = true,
) {
    val view = LocalView.current
    val colors = if (isAvailable) {
        ButtonDefaults.textButtonColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer,
            contentColor = MaterialTheme.colorScheme.onPrimaryContainer,
        )
    } else {
        ButtonDefaults.textButtonColors(
            containerColor = MaterialTheme.colorScheme.surface,
            contentColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.38f)
        )
    }
    Button(
        onClick = { onClick(view) },
        modifier = modifier
            .height(64.dp)
            .width(180.dp)
            .padding(4.dp),
        colors = colors
    ) {
        Text(text = label)
    }
}

@SuppressLint("InlinedApi")
@Preview(showBackground = true)
@Composable
fun HomeScreenPreview() {
    HomeScreen(
        homeUiState = HomeUiState(
            listOf(
                HapticCategory(
                    "Effects",
                    categoryType = HapticCategoryType.PREDEFINED_EFFECTS,
                    buttons = listOf(
                        HapticButton(
                            "Tick", true, VibrationEffect.EFFECT_TICK,
                        ),
                        HapticButton(
                            "Click", false, VibrationEffect.EFFECT_CLICK,
                        ),
                    ),
                ),
                HapticCategory(
                    "Primitives",
                    categoryType = HapticCategoryType.COMPOSITION_PRIMITIVES,
                    buttons = listOf(
                        HapticButton(
                            "Spin",
                            true,
                            VibrationEffect.Composition.PRIMITIVE_SPIN,
                        ),
                        HapticButton(
                            "Thud",
                            false,
                            VibrationEffect.Composition.PRIMITIVE_THUD,
                        ),
                    ),
                ),
            ),
        ),
        onButtonClicked = { _, _, _ -> },
        onShowMessage = {},
    )
}
