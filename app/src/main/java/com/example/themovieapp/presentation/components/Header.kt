package com.example.themovieapp.presentation.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.themovieapp.R
import com.example.themovieapp.presentation.navigation.Screen
import com.example.themovieapp.utils.TabPage

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Header(
    title: Any,
    navController: NavController,
    tabPage: TabPage,
    showIcon: Boolean = true,
    showBackButton: Boolean = false,
    navigateOnClick: (() -> Unit)? = null,
    content: @Composable (PaddingValues) -> Unit
) {
    var tabPage = tabPage
    Scaffold(topBar = {
        TopAppBar(title = {
            Row(
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .padding(2.dp)
                    .fillMaxWidth()
            ) {
                if (showIcon) {
                    Image(
                        painter = painterResource(R.drawable.movie_icon),
                        contentDescription = "",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .size(36.dp)
                            .clip(
                                CircleShape
                            )
                            .clickable {
                                navController.navigate(Screen.HomeScreen.route)
                            }
                    )
                }
                Text(
                    text = if (title is Int) stringResource(title) else title.toString(),
                    color = MaterialTheme.colorScheme.secondary,
                    style = MaterialTheme.typography.headlineMedium,
                )
            }
        },
            navigationIcon = {
                if (showBackButton) {
                    IconButton(onClick = {
                        if (navigateOnClick != null) {
                            navigateOnClick()
                        }
                    }) {
                        Icon(
                            imageVector = Icons.Default.ArrowBackIosNew,
                            contentDescription = ""
                        )
                    }
                }
            })
    },
        bottomBar = {
            BottomAppBar {
                BottomTab(navController = navController,
                    currentPage = tabPage,
                    onTabSelected = { tabPage = it })
            }

        }) { paddingValue ->
        content(paddingValue)
    }
}