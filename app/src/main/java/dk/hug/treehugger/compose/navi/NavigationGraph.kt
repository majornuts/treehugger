package dk.hug.treehugger.compose.navi

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import dk.hug.treehugger.compose.ScreenAbout
import dk.hug.treehugger.compose.ScreenHeat
import dk.hug.treehugger.compose.ScreenMap
import dk.hug.treehugger.compose.ScreenMapViewModel


val screenMapViewModel = ScreenMapViewModel()

@Composable
fun NavigationGraph(
    navController: NavHostController
) {
    NavHost(navController, startDestination = NavItems.Map.screen_route) {
        composable(NavItems.Map.screen_route) {
            ScreenMap(screenMapViewModel)
        }
        composable(NavItems.List.screen_route) {
            ScreenHeat(screenMapViewModel)
        }
        composable(NavItems.About.screen_route) {
            ScreenAbout()
        }
    }
}

@Composable
fun BottomAppBarWithNavigation(navController: NavHostController, modifier: Modifier = Modifier) {
    BottomAppBar() {
        Row(modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
            NavigationItem(navController, NavItems.Map)
            NavigationItem(navController, NavItems.List)
            NavigationItem(navController, NavItems.About)
        }
    }
}

@Composable
fun NavigationItem(
    navController: NavHostController,
    screen: NavItems,
) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    IconButton(
        onClick = {
            navController.navigate(screen.screen_route)
        }
    ) {
        Icon(
            imageVector = screen.icon,
            contentDescription = screen.title,
            tint = if (screen.screen_route == currentDestination?.route) Color(0xFF0B6411) else Color.Gray
        )
    }
}