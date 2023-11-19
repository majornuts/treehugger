package dk.hug.treehugger.compose

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.util.Log
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.core.app.ActivityCompat
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.TileOverlayOptions
import com.google.maps.android.compose.CameraPositionState
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapEffect
import com.google.maps.android.heatmaps.HeatmapTileProvider
import com.google.maps.android.ktx.buildGoogleMapOptions
import dk.hug.treehugger.core.DBhandler

@Composable
fun ScreenHeat(screenMapViewModel: ScreenMapViewModel) {
    drawHeatMap(screenMapViewModel)
}

@Composable
fun drawHeatMap(viewModel: ScreenMapViewModel) {
    val current = androidx.compose.ui.platform.LocalContext.current


    GoogleMap(
        modifier = Modifier.fillMaxSize(), cameraPositionState = CameraPositionState(
            viewModel.get()
        )
    ) {
        MapEffect { map ->
            map.setOnCameraIdleListener {
                list.clear()
                addHeatMap(map)
                viewModel.onMapPosChange(map.cameraPosition)
            }
            buildGoogleMapOptions {

                if (ActivityCompat.checkSelfPermission(
                        current,
                        Manifest.permission.ACCESS_FINE_LOCATION
                    ) == PackageManager.PERMISSION_GRANTED || ActivityCompat.checkSelfPermission(
                        current,
                        Manifest.permission.ACCESS_COARSE_LOCATION
                    ) == PackageManager.PERMISSION_GRANTED
                ) {
                    map.isMyLocationEnabled = true
                }
                map.uiSettings.isMyLocationButtonEnabled = true
                map.uiSettings.isZoomControlsEnabled = true
                map.uiSettings.isZoomGesturesEnabled = true
                map.uiSettings.isScrollGesturesEnabled = true
                map.uiSettings.isRotateGesturesEnabled = false
                map.uiSettings.isTiltGesturesEnabled = false
            }
        }

    }
}


private fun addHeatMap(googleMap: GoogleMap) {

    val data = GatheringTrees(googleMap)
    Log.d("data", data.size.toString())
    val provider = HeatmapTileProvider.Builder().data(data).build()
    googleMap.addTileOverlay(TileOverlayOptions().tileProvider(provider))
}

val list = MutableList<LatLng>(0) { LatLng(0.0, 0.0) }
private fun GatheringTrees(map: GoogleMap): MutableList<LatLng> {
    DBhandler.getRegionTreeList(map.projection).forEach {
        list.add(LatLng(it.lat, it.lon))
    }
    if (list.isEmpty()) {
        list.add(LatLng(55.678814, 12.564026))
    }
    return list
}