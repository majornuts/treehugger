package dk.hug.treehugger;

import android.content.Context;
import android.os.AsyncTask;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.TileOverlayOptions;
import com.google.maps.android.heatmaps.HeatmapTileProvider;

import java.util.ArrayList;

import dk.hug.treehugger.core.DBhandler;
import dk.hug.treehugger.core.Tree;
import dk.hug.treehugger.model.Feature;

class HeatMapLoader extends AsyncTask<Void, Void, Void> {
    private HeatmapTileProvider mProvider;
    private Context context;
    private HeatMapLoaderCallback callback;

    public HeatMapLoader(Context context, HeatMapLoaderCallback callback) {
        this.context = context;
        this.callback = callback;
    }

    @Override
    protected Void doInBackground(Void... params) {

        ArrayList<LatLng> list = new ArrayList<>();
//        for (Feature feature : DBhandler.getTrees(context).getFeatures()) {
//            double lat = feature.getGeometry().getCoordinates().get(1);
//            double lng = feature.getGeometry().getCoordinates().get(0);
//            LatLng geo = new LatLng(lat, lng);
//            list.add(geo);
//        }
        for (Tree tree : DBhandler.getTreeList(context)) {
            double lat = tree.getLat();
            double lng = tree.getLon();
            LatLng geo = new LatLng(lat, lng);
            list.add(geo);
        }

        mProvider = new HeatmapTileProvider.Builder()
                .data(list)
                .build();

        callback.updateHeatMap(new TileOverlayOptions().tileProvider(mProvider));
        return null;
    }
}
