package dk.hug.treehugger.compose.navi

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBox
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector

sealed class NavItems(var title: String, var icon: ImageVector, var screen_route: String) {

    val tint: Color = Color(0xFF477217)

    object Home : NavItems("Home", Icons.Default.Home, "Home")
    object Map : NavItems("Map", Icons.Default.LocationOn, "Map")
    object List : NavItems("List", Icons.Default.LocationOn, "List")
    object About : NavItems("About", Icons.Default.AccountBox, "About")
}