package dk.hug.treehugger;

import android.os.AsyncTask;

import com.google.android.gms.maps.Projection;
import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.List;

import dk.hug.treehugger.core.DBhandler;
import dk.hug.treehugger.core.Tree;
import dk.hug.treehugger.model.Pos;

public class MapLoader extends AsyncTask<Void, Void, List<Pos>> {
    private static final String TAG = "MapLoader";
    private final Projection projection;
    private MapLoaderCallback callback;

    public MapLoader(MapLoaderCallback context, Projection projection) {
        this.callback = context;
        this.projection = projection;
    }

    @Override
    protected List<Pos> doInBackground(Void... params) {
        List<Tree> treeList = DBhandler.getRegionTreeList(callback.getActivityContext(), projection);
        List<Pos> posList = new ArrayList<>();
        for (Tree tree : treeList) {
            double lat = tree.getLat();
            double lng = tree.getLon();
            LatLng geo = new LatLng(lat, lng);
            Pos pos = new Pos(tree.getDanishName(), tree.getSpecies(), geo);
            posList.add(pos);
        }
        return posList;
    }

    @Override
    protected void onPostExecute(List<Pos> pos) {
        callback.updateMap(pos);
        callback = null;
    }
}
