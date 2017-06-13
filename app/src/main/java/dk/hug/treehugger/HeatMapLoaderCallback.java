package dk.hug.treehugger;

import com.google.android.gms.maps.model.TileOverlayOptions;

public interface HeatMapLoaderCallback {
    void updateHeatMap(TileOverlayOptions overlayOptions);
    void startLoader();
    void stopLoader();
}
