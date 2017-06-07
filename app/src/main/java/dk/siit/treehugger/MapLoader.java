package dk.siit.treehugger;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.google.android.gms.maps.Projection;
import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.clustering.ClusterManager;

import java.util.ArrayList;
import java.util.List;

import dk.siit.treehugger.core.DBhandler;
import dk.siit.treehugger.core.Tree;
import dk.siit.treehugger.model.Pos;

public class MapLoader extends AsyncTask<Void, Void, Void> {
    private static final String TAG = "MapLoader";
    private final ClusterManager<Pos> posClusterManager;
    private final Projection projection;
    private Context context;
    private List<Tree> treeList;
    private ArrayList<Pos> posList;

    public MapLoader(Context context, ClusterManager<Pos> mClusterManager, Projection projection) {
        this.context = context;
        posClusterManager = mClusterManager;
        this.projection = projection;

    }

    @Override
    protected Void doInBackground(Void... params) {
        treeList = DBhandler.getRegionTreeList(context, projection);
        posList = new ArrayList<Pos>();
        for (Tree tree : treeList) {
            double lat = tree.getLat();
            double lng = tree.getLon();
            LatLng geo = new LatLng(lat, lng);
            Pos pos = new Pos(tree.getDanishName(), tree.getSpecies(), geo);
            posList.add(pos);
        }
        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        posClusterManager.clearItems();
        posClusterManager.cluster();
        posClusterManager.addItems(posList);
    }

}
