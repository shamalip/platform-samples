/*
 * Copyright 2023 The Android Open Source Project
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

package com.example.platform.ui.appwidgets.glance


import android.content.res.Resources
import android.os.Build
import androidx.annotation.StringRes
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.glance.GlanceModifier
import androidx.glance.LocalContext
import androidx.glance.appwidget.components.Scaffold
import androidx.glance.appwidget.cornerRadius
import androidx.glance.layout.Alignment
import androidx.glance.layout.Box
import androidx.glance.layout.Column
import androidx.glance.layout.ColumnScope
import androidx.glance.layout.fillMaxSize
import androidx.glance.layout.padding

/**
 * Provide a Box composable using the system parameters for app widgets background with rounded
 * corners and background color.
 */
@Composable
fun AppWidgetBox(
    modifier: GlanceModifier = GlanceModifier,
    contentAlignment: Alignment = Alignment.TopStart,
    content: @Composable () -> Unit,
) {
    Scaffold {
        Box(
            modifier = modifier.fillMaxSize(),
            contentAlignment = contentAlignment,
        ) {
            content()
        }
    }
}

/**
 * Provide a Column composable using the system parameters for app widgets background with rounded
 * corners and background color.
 */
@Composable
fun AppWidgetColumn(
    modifier: GlanceModifier = GlanceModifier,
    verticalAlignment: Alignment.Vertical = Alignment.Top,
    horizontalAlignment: Alignment.Horizontal = Alignment.Start,
    content: @Composable ColumnScope.() -> Unit,
) {
    Scaffold(
        modifier = GlanceModifier.padding(vertical = widgetPadding)
    ) {
        Column(
            modifier = modifier,
            verticalAlignment = verticalAlignment,
            horizontalAlignment = horizontalAlignment,
            content = content,
        )
    }
}

/**
 * Applies corner radius for views that are visually positioned [widgetPadding]dp inside of the
 * widget background.
 */
@Composable
fun GlanceModifier.appWidgetInnerCornerRadius(): GlanceModifier {
    if (Build.VERSION.SDK_INT < 31) {
        return this
    }
    val resources = LocalContext.current.resources
    // get dimension in float (without rounding).
    val px = resources.getDimension(android.R.dimen.system_app_widget_background_radius)
    val widgetBackgroundRadiusDpValue = px / resources.displayMetrics.density
    if (widgetBackgroundRadiusDpValue < widgetPadding.value) {
        return this
    }
    return this.cornerRadius(Dp(widgetBackgroundRadiusDpValue - widgetPadding.value))
}

@Composable
fun stringResource(@StringRes id: Int, vararg args: Any): String {
    return LocalContext.current.getString(id, args)
}

val Float.toPx get() = this * Resources.getSystem().displayMetrics.density
val widgetPadding = 12.dp
