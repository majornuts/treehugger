package dk.hug.treehugger

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.core.app.ActivityCompat
import androidx.navigation.compose.rememberNavController
import dk.hug.treehugger.compose.data.TreeDownloaderKotlin
import dk.hug.treehugger.compose.navi.BottomAppBarWithNavigation
import dk.hug.treehugger.compose.navi.NavigationGraph
import dk.hug.treehugger.core.DBhandler

class MainActivity : ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        DBhandler().init(this)
        TreeDownloaderKotlin().doStuff()

        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this, Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            requestPermissions(
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ), 1
            )

        }
        setContent {
            Scaffold()
        }
    }
}

@Composable
fun Scaffold() {
    val navHostController = rememberNavController()
    Scaffold(
        content = { paddingValues ->
            Box(modifier = Modifier.padding(paddingValues)) {
                NavigationGraph(navController = navHostController)
            }
        }, bottomBar = {
            BottomAppBarWithNavigation(
                navController = navHostController, modifier = Modifier.fillMaxWidth()
            )
        })
}
