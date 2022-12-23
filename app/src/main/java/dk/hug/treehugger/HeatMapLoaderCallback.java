package dk.hug.treehugger;

import android.content.Context;

import com.google.android.gms.maps.model.TileOverlayOptions;

public interface HeatMapLoaderCallback {
    Context getActivityContext();

    void updateHeatMap(TileOverlayOptions overlayOptions);
    void startLoader();
    void stopLoader();
}
