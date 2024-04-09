package com.example.themovieapp.presentation.components

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.themovieapp.presentation.navigation.Screen
import com.example.themovieapp.utils.TabPage

@Composable
fun BottomTab(
    navController: NavController,
    tabPage: TabPage,
    onTabSelected: (tabPage: TabPage) -> Unit

) {
    TabRow(
        selectedTabIndex = tabPage.ordinal,
        modifier = Modifier
            .fillMaxWidth(),

        containerColor = MaterialTheme.colorScheme.onSecondaryContainer,
        contentColor = MaterialTheme.colorScheme.onPrimary,
    ) {

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceEvenly,

        ) {
            SingleTab(title = "Home", icon = Icons.Default.Home, onclick = {
                onTabSelected(TabPage.HOME)
                navController.navigate(
                Screen.HomeScreen.route
            )})
            SingleTab(title = "Favourite", icon = Icons.Default.Favorite, onclick = {
                onTabSelected(TabPage.FAVOURITE)
                navController.navigate(
                Screen.FavouriteScreen.route
            )})
        }
    }
}

@Composable
private fun SingleTab(
    onclick : () -> Unit,
    icon: ImageVector,
    title: String

) {
    Row(
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .padding(16.dp)
            .clickable {
                onclick()
            }
    ) {
        Icon(imageVector = icon, contentDescription = null)
        Spacer(modifier = Modifier.width(16.dp))
        Text(text = title)
    }

}