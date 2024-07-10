package dk.hug.treehugger.compose

import android.Manifest
import android.content.ContentValues.TAG
import android.content.pm.PackageManager
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.app.ActivityCompat
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.CameraPositionState
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapEffect
import com.google.maps.android.compose.MapsComposeExperimentalApi
import com.google.maps.android.compose.clustering.Clustering
import com.google.maps.android.ktx.buildGoogleMapOptions
import dk.hug.treehugger.R
import dk.hug.treehugger.compose.data.MyItem
import dk.hug.treehugger.core.DBhandler


var items = mutableStateListOf<MyItem>()

@Composable
fun ScreenMap(viewModel: ScreenMapViewModel) {
    GoogleMapClustering(viewModel = viewModel)
}

@OptIn(MapsComposeExperimentalApi::class)
@Composable
fun GoogleMapClustering(viewModel: ScreenMapViewModel) {
    val current = androidx.compose.ui.platform.LocalContext.current
    GoogleMap(
        modifier = Modifier.fillMaxSize(), cameraPositionState = CameraPositionState(
            viewModel.get()
        )
    ) {
        MapEffect { map ->
            map.setOnCameraIdleListener {
                viewModel.onMapPosChange(map.cameraPosition)
                items.clear()
                GatheringTrees(map)
            }
            buildGoogleMapOptions {
                if (ActivityCompat.checkSelfPermission(
                        current, Manifest.permission.ACCESS_FINE_LOCATION
                    ) == PackageManager.PERMISSION_GRANTED || ActivityCompat.checkSelfPermission(
                        current, Manifest.permission.ACCESS_COARSE_LOCATION
                    ) == PackageManager.PERMISSION_GRANTED
                ) {
                    map.isMyLocationEnabled = true
                }
                map.uiSettings.isMyLocationButtonEnabled = true
                map.uiSettings.isZoomControlsEnabled = true
                map.uiSettings.isZoomGesturesEnabled = true
                map.uiSettings.isScrollGesturesEnabled = true
                map.uiSettings.isRotateGesturesEnabled = false
                map.uiSettings.isTiltGesturesEnabled = true

            }
        }
        Clustering(
            clusterItemContent = {
                Icon(
                    bitmap = ImageBitmap.imageResource(R.drawable.tree),
                    contentDescription = "Info",
                    tint = Color(0xFF0B6411),
                )
            },
            items = items,
            onClusterClick = { cluster ->
                false
            },
            onClusterItemClick = {
                Log.d(TAG, "Cluster item clicked! $it")

                false
            },
            onClusterItemInfoWindowClick = {
                Log.d(TAG, "Cluster item info window clicked! $it")
            },
            clusterContent = { cluster ->
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .padding(5.dp, 0.dp, 5.dp, 5.dp)
                        .size(40.dp)
                        .background(Color(0xFF0B6411), CircleShape)
                ) {
                    Text(
                        text = cluster.size.toString(),
                        fontSize = 16.sp,
                        color = Color.White,
                        textAlign = TextAlign.Center
                    )
                }
            },
        )
    }
}

private fun GatheringTrees(map: GoogleMap) {
    DBhandler.getRegionTreeList(map.projection).forEach {
        val dkname = if (it.danishName == null) {
            "No danish name"
        } else {
            it.danishName
        }
        val latin = if (it.species == null) {
            "No latin name"
        } else {
            it.species
        }
        items.add(
            MyItem(
                LatLng(it.lat, it.lon), dkname, latin, 0f
            )
        )
    }
}

