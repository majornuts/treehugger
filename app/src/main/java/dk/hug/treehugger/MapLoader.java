package dk.hug.treehugger;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.androidmapsextensions.MarkerOptions;
import com.google.android.gms.maps.model.LatLng;

import java.util.List;

import dk.hug.treehugger.core.DBhandler;
import dk.hug.treehugger.core.Tree;
import dk.hug.treehugger.model.Feature;
import dk.hug.treehugger.model.Pos;
import dk.hug.treehugger.model.Root;

public class MapLoader extends AsyncTask<Void, Void, Void> {
    private static final String TAG = "MapLoader";
    private Context context;
    private MapLoaderCallback callback;

    public MapLoader(Context context, MapLoaderCallback callback) {
        this.context = context;
        this.callback = callback;
    }

    @Override
    protected Void doInBackground(Void... params) {
        long time = System.currentTimeMillis();
//        Root root = DBhandler.getTrees(context);
//        Log.d(TAG, "load time:" + (System.currentTimeMillis() - time));
//        for (int i = 0; i < root.getFeatures().size(); i++) {
//            Feature feature = root.getFeatures().get(i);
//            double lat = feature.getGeometry().getCoordinates().get(1);
//            double lng = feature.getGeometry().getCoordinates().get(0);
//            LatLng geo = new LatLng(lat, lng);
//
//            Pos pos = new Pos(feature.getProperties().getDanskNavn(), geo);
//            final MarkerOptions marker = new MarkerOptions()
//                    .position(pos.getPosition()).title(pos.getName())
//                    .snippet(feature.getProperties().getTraeArt());
//
//            callback.plantTreeOnMap(marker);
//        }

        List<Tree> treeList = DBhandler.getTreeList(context);
        Log.d(TAG, "load time:" + (System.currentTimeMillis() - time));
        for (Tree tree:treeList) {
            double lat = tree.getLat();
            double lng = tree.getLon();
            LatLng geo = new LatLng(lat, lng);

            Pos pos = new Pos(tree.getDanishName(), geo);
            final MarkerOptions marker = new MarkerOptions()
                    .position(pos.getPosition()).title(pos.getName())
                    .snippet(tree.getSpecies());

            callback.plantTreeOnMap(marker);
        }

        Log.d(TAG, "draw time " + (System.currentTimeMillis() - time));
        return null;
    }
}
