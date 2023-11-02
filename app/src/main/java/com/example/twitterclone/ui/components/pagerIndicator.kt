package com.example.twitterclone.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.dp


@Composable
fun CircularPageIndicator(
    numberOfPages: Int,
    currentPage: Int,
    modifier: Modifier = Modifier
) {
    Canvas(
        modifier = modifier.padding(8.dp)
    ) {
        val circleRadius = 10f // Radius of each circle
        val circleSpacing = 30f // Spacing between circles
        val indicatorRadius = 15f // Radius of the indicator circle
        val centerY = size.height / 2f
        val startX = (size.width - (numberOfPages * (2 * circleRadius + circleSpacing))) / 2f

        for (i in 0 until numberOfPages) {
            val x = startX + i * (2 * circleRadius + circleSpacing)
            drawCircle(
                color = if (i == currentPage) Color(0xff24A7DE) else Color(0xFF606061),
                radius = circleRadius,
                center = Offset(x + circleRadius, centerY),
                style = Stroke(2f)
            )
        }

        // Draw the indicator circle
        val indicatorX = startX + currentPage * (2 * circleRadius + circleSpacing)
        drawCircle(
            color = Color(0xff24A7DE),
            radius = indicatorRadius,
            center = Offset(indicatorX + circleRadius, centerY)
        )
    }
}