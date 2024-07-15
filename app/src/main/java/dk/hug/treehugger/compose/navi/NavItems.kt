package dk.hug.treehugger.compose.navi

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBox
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.ui.graphics.vector.ImageVector

sealed class NavItems(var title: String, var icon: ImageVector, var screen_route: String) {

    data object Map : NavItems("Map", Icons.Default.Home, "Map")
    data object List : NavItems("List", Icons.Default.LocationOn, "List")
    data object About : NavItems("About", Icons.Default.AccountBox, "About")
}