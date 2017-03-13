package dk.hug.treehugger;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.androidmapsextensions.ClusteringSettings;
import com.androidmapsextensions.GoogleMap;
import com.androidmapsextensions.MarkerOptions;
import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.model.LatLng;

import dk.hug.treehugger.core.DBhandler;
import dk.hug.treehugger.core.application;
import dk.hug.treehugger.model.Feature;
import dk.hug.treehugger.model.Pos;
import dk.hug.treehugger.model.Root;

public class MapsActivity extends AppCompatActivity {

    private static final String TAG = "MapsActivity";
    private GoogleMap mMap;

    private GoogleApiClient client;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_maps);

        progressDialog = new ProgressDialog(this);

        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
        mMap = ((com.androidmapsextensions.SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map)).getExtendedMap();
        mMap.getUiSettings().setRotateGesturesEnabled(false);
        mMap.getUiSettings().setTiltGesturesEnabled(false);
        mMap.getUiSettings().setMyLocationButtonEnabled(true);
        LatLng dis = new LatLng(55.678814, 12.564026);
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(dis, 14));

        mMap.setClustering(new ClusteringSettings().enabled(true).addMarkersDynamically(true).clusterSize(75));

        if (DBhandler.getTreeState(this) != 0) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("do you want to download trees?")
                    .setNegativeButton("no", null)
                    .setPositiveButton("yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            new TreeDownload(MapsActivity.this, new Handler.Callback() {
                                @Override
                                public boolean handleMessage(Message msg) {
                                    if(msg.getData().getBoolean("isDone")){
                                        new MapLoader().execute();
                                    }
                                    return false;
                                }
                            }).execute();


                        }
                    }).show();
        } else {
            new MapLoader().execute();
        }
    }


    @Override
    public void onStart() {
        super.onStart();

        client.connect();
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "Maps Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app URL is correct.
                Uri.parse("android-app://dk.hug.treehugger/http/host/path")
        );
        AppIndex.AppIndexApi.start(client, viewAction);
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "Maps Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app URL is correct.
                Uri.parse("android-app://dk.hug.treehugger/http/host/path")
        );
        AppIndex.AppIndexApi.end(client, viewAction);
        client.disconnect();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.input_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.map:

                break;
            case R.id.heatmap:
                startActivity(new Intent(this, HeatMapsActivity.class));
                break;
            case R.id.updateTrees:
                break;
        }

        return true;
    }

    class MapLoader extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... params) {
            Root root = DBhandler.getTrees(MapsActivity.this);
            for (int i = 0; i < root.getFeatures().size(); i++) {
                Feature feature = root.getFeatures().get(i);
                double lat = feature.getGeometry().getCoordinates().get(1);
                double lng = feature.getGeometry().getCoordinates().get(0);
                LatLng geo = new LatLng(lat, lng);

                Pos pos = new Pos(feature.getProperties().getDanskNavn(), geo);
                final MarkerOptions marker = new MarkerOptions()
                        .position(pos.getPosition()).title(pos.getName())
                        .snippet(feature.getProperties().getTraeArt());
                MapsActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mMap.addMarker(marker);
                    }
                });
            }
            return null;
        }

        @Override
        protected void onPreExecute() {
            progressDialog.setMessage("Planting trees on the map");
            progressDialog.show();
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            progressDialog.dismiss();
        }
    }
}


