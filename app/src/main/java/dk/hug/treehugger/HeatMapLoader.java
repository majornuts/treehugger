package dk.hug.treehugger;

import android.os.AsyncTask;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.TileOverlayOptions;
import com.google.maps.android.heatmaps.HeatmapTileProvider;

import java.util.ArrayList;
import java.util.List;

import dk.hug.treehugger.core.DBhandler;
import dk.hug.treehugger.core.Tree;

class HeatMapLoader extends AsyncTask<Void, Void, List<LatLng>> {
    private HeatmapTileProvider mProvider;
    private HeatMapLoaderCallback callback;

    public HeatMapLoader(HeatMapLoaderCallback callback) {
        this.callback = callback;
    }

    @Override
    protected List<LatLng> doInBackground(Void... params) {

        ArrayList<LatLng> list = new ArrayList<>();

        for (Tree tree : DBhandler.getTreeList()) {
            double lat = tree.getLat();
            double lng = tree.getLon();
            LatLng geo = new LatLng(lat, lng);
            list.add(geo);
        }

        return list;
    }

    @Override
    protected void onPostExecute(List<LatLng> list) {
        if (!list.isEmpty()) {
            mProvider = new HeatmapTileProvider.Builder()
                    .data(list)
                    .build();
            callback.updateHeatMap(new TileOverlayOptions().tileProvider(mProvider));
        }
        callback = null;
    }
}
