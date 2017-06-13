package dk.hug.treehugger;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.TileOverlayOptions;
import com.google.maps.android.heatmaps.HeatmapTileProvider;

import java.util.ArrayList;
import java.util.List;

import dk.hug.treehugger.core.DBhandler;
import dk.hug.treehugger.core.Tree;
import dk.hug.treehugger.model.Feature;

class HeatMapLoader extends AsyncTask<Void, Void, List<LatLng>> {
    private HeatmapTileProvider mProvider;
    private Context context;
    private HeatMapLoaderCallback callback;

    public HeatMapLoader(Context context, HeatMapLoaderCallback callback) {
        this.context = context;
        this.callback = callback;


    }

    @Override
    protected List<LatLng> doInBackground(Void... params) {

        ArrayList<LatLng> list = new ArrayList<>();

        for (Tree tree : DBhandler.getTreeList(context)) {
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
    }
}
