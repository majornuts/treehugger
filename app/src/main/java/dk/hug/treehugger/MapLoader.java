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
import dk.hug.treehugger.model.Pos;

public class MapLoader extends AsyncTask<Void, Void, Void> {
    private static final String TAG = "MapLoader";
    private ProgressDialog progressDialog;
    private Context context;
    private MapLoaderCallback callback;

    public MapLoader(Context context, MapLoaderCallback callback) {
        this.context = context;
        this.callback = callback;

        progressDialog = new ProgressDialog(context);
        progressDialog.setMessage("planting trees");
        progressDialog.setCancelable(false);
        progressDialog.show();

    }

    @Override
    protected Void doInBackground(Void... params) {
        long time = System.currentTimeMillis();

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

    @Override
    protected void onPostExecute(Void aVoid) {
        progressDialog.dismiss();
    }

    @Override
    protected void onCancelled() {
        progressDialog.dismiss();
        progressDialog = null;
        super.onCancelled();
    }

}
