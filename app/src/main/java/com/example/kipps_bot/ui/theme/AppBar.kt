package com.example.kipps_bot.ui.theme

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MediumTopAppBar
import androidx.compose.material3.SmallTopAppBar
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppBar(
    title: String,
    modifier: Modifier = Modifier
) {

    val gradientColors = listOf(
        PrimaryColor,
        AccentColor
    )
    SmallTopAppBar(
        title = {
            Text(
                text = title,
                color = Color.White,
                fontSize = 25.sp
            )
        },
        modifier = modifier
            .fillMaxWidth()
            .background(
                brush = Brush.verticalGradient(gradientColors),
                shape = RoundedCornerShape(
                    bottomEnd = 10.dp,
                    bottomStart = 10.dp
                ),
            ),
        colors = TopAppBarDefaults.mediumTopAppBarColors(containerColor = Color.Transparent),
    )
}

@Preview(showBackground = true)
@Composable
fun AppBarPreview() {
    AppBar(
        title = "App Title",
    )
}
