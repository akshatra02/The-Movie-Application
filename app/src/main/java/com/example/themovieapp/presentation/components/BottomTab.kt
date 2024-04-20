package com.example.themovieapp.presentation.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.themovieapp.R
import com.example.themovieapp.presentation.navigation.Screen
import com.example.themovieapp.utils.TabPage

@Composable
fun BottomTab(
    navController: NavController, currentPage: TabPage, onTabSelected: (tabPage: TabPage) -> Unit

) {
    TabRow(
        selectedTabIndex = currentPage.ordinal,
        containerColor = MaterialTheme.colorScheme.background,
        contentColor = MaterialTheme.colorScheme.secondary,
    ) {
        TabItem(title = stringResource(R.string.home),
            icon = Icons.Default.Home,
            selected = currentPage == TabPage.HOME,
            onclick = {
                onTabSelected(TabPage.HOME)
                navController.navigate(
                    Screen.HomeScreen.route
                )
                {
                    navController.navigate(Screen.HomeScreen.route) {
                        popUpTo(Screen.HomeScreen.route) {
                            inclusive = true
                        }
                    }
                }
            })
        TabItem(
            title = stringResource(R.string.search),
            icon = Icons.Default.Search,
            selected = currentPage == TabPage.SEARCH,
            onclick = {
                onTabSelected(TabPage.SEARCH)
                navController.navigate(
                    Screen.SearchScreen.route
                ) {
                    navController.navigate(Screen.SearchScreen.route) {
                        popUpTo(Screen.SearchScreen.route) {
                            inclusive = true
                        }
                    }
                }
            })
        TabItem(
            title = stringResource(R.string.favourite),
            icon = Icons.Default.Favorite,
            selected = currentPage == TabPage.FAVOURITE,
            onclick = {
                onTabSelected(TabPage.FAVOURITE)
                navController.navigate(
                    Screen.FavouriteScreen.route
                ) {
                    navController.navigate(Screen.FavouriteScreen.route) {
                        popUpTo(Screen.FavouriteScreen.route) {
                            inclusive = true
                        }
                    }
                }
            })
    }
}

@Composable
private fun TabItem(
    onclick: () -> Unit, selected: Boolean, icon: ImageVector, title: String

) {
    Column(verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .padding(16.dp)
            .clickable {
                onclick()
            }) {
        Icon(
            imageVector = icon, contentDescription = null,
            tint = if (selected) MaterialTheme.colorScheme.tertiary else MaterialTheme.colorScheme.secondary
        )
        Spacer(modifier = Modifier.width(16.dp))
        Text(
            text = title,
            color = if (selected) MaterialTheme.colorScheme.tertiary else MaterialTheme.colorScheme.secondary
        )
    }

}