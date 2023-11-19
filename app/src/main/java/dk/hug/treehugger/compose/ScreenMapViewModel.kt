package dk.hug.treehugger.compose

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng

class ScreenMapViewModel : ViewModel() {

    val dis = CameraPosition(LatLng(55.678814, 12.564026), 14f, 0f, 0f)


    private val mapPosition = MutableLiveData<CameraPosition>()

    var mapPos: LiveData<CameraPosition> = mapPosition
    fun onMapPosChange(mapPos: CameraPosition) {
        mapPosition.value = mapPos
    }

    fun get(): CameraPosition {
        if (mapPosition.value == null) {
            mapPosition.value = dis
        } else {
            mapPosition.value = mapPosition.value
        }
        return mapPosition.value!!
    }
}