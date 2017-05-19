package dk.hug.treehugger;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.RequiresApi;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.RelativeLayout;

import com.androidmapsextensions.ClusteringSettings;
import com.androidmapsextensions.GoogleMap;
import com.androidmapsextensions.MarkerOptions;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.model.LatLng;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import dk.hug.treehugger.core.DBhandler;
import dk.hug.treehugger.model.Feature;
import dk.hug.treehugger.model.Pos;
import dk.hug.treehugger.model.Root;

public class MapsActivity extends AppCompatActivity implements MapLoaderCallback {

    private static final String TAG = "MapsActivity";
    private GoogleMap mMap;
    private RelativeLayout mRootView;

    private GoogleApiClient client;
    private MapTasks mapTasks;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_maps);

        mRootView = (RelativeLayout) findViewById(R.id.rootView);

        MobileAds.initialize(getApplicationContext(), this.getResources().getString(R.string.unit_id));

        AdView mAdView = (AdView) findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().addTestDevice(AdRequest.DEVICE_ID_EMULATOR).build();
        mAdView.loadAd(adRequest);

        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
        initmap();

        final MapLoader mapLoader = new MapLoader(MapsActivity.this, MapsActivity.this);
        final TreeDownload treeDownload = new TreeDownload(MapsActivity.this, new Handler.Callback() {
            @Override
            public boolean handleMessage(Message msg) {
                if (msg.getData().getBoolean("isDone")) {
                    mMap.clear();
                    mapLoader.execute();
                }
                return false;
            }
        });

        if(getLastCustomNonConfigurationInstance()==null) {
            mapTasks = new MapTasks(treeDownload, mapLoader);

            if (DBhandler.getTreeState(this) != 1) {
                if(checkConnectivity()) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(this);
                    builder.setMessage("do you want to download trees?")
                            .setNegativeButton("no", null)
                            .setPositiveButton("yes", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    treeDownload.execute();
                                }
                            }).show();
                } else {
                    showNoInternet();
                }
            } else {
                mapLoader.execute();
            }
        } else {
            mapTasks = (MapTasks) getLastCustomNonConfigurationInstance();
            if(mapTasks.getMapLoader().getStatus()== AsyncTask.Status.RUNNING||
                    mapTasks.getMapLoader().getStatus()== AsyncTask.Status.PENDING)
                mapTasks.getMapLoader().cancel(true);

            mapTasks.setMapLoader(new MapLoader(MapsActivity.this, MapsActivity.this));
            mapTasks.getMapLoader().execute();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private boolean hasPermission(String perm) {
        return (PackageManager.PERMISSION_GRANTED == checkSelfPermission(perm));
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private boolean canAccessLocation() {
        return (hasPermission(Manifest.permission.ACCESS_FINE_LOCATION));
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void initmap() {
        mMap = ((com.androidmapsextensions.SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map)).getExtendedMap();

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{
                            Manifest.permission.ACCESS_FINE_LOCATION,
                            Manifest.permission.ACCESS_COARSE_LOCATION},
                    2);
        }
        if (canAccessLocation()) {
            mMap.setMyLocationEnabled(true);
        }
        mMap.clear();
        mMap.getUiSettings().setRotateGesturesEnabled(false);
        mMap.getUiSettings().setTiltGesturesEnabled(false);
        mMap.getUiSettings().setMyLocationButtonEnabled(true);
        LatLng dis = new LatLng(55.678814, 12.564026);
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(dis, 14));
        mMap.setClustering(new ClusteringSettings().enabled(true).addMarkersDynamically(true).clusterSize(75));
    }

    private boolean checkConnectivity() {
        ConnectivityManager cm =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        return activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();
    }

    private void showNoInternet() {
        Snackbar.make(mRootView, R.string.no_internet, Snackbar.LENGTH_LONG).show();
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
    public MapTasks onRetainCustomNonConfigurationInstance() {
        return mapTasks;
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
            case R.id.Heatmap:
                startActivity(new Intent(this, HeatMapsActivity.class));
                break;
            case R.id.updateTrees:
                if(checkConnectivity()) {
                    if(mapTasks.getTreeDownload().getStatus()!=AsyncTask.Status.RUNNING &&
                            mapTasks.getMapLoader().getStatus()!= AsyncTask.Status.RUNNING) {
                        final MapLoader mapLoader = new MapLoader(MapsActivity.this, MapsActivity.this);
                        final TreeDownload treeDownload = new TreeDownload(MapsActivity.this, new Handler.Callback() {
                            @Override
                            public boolean handleMessage(Message msg) {
                                if (msg.getData().getBoolean("isDone")) {
                                    mMap.clear();
                                    mapLoader.execute();
                                }
                                return false;
                            }
                        });
                        mapTasks.setTreeDownload(treeDownload);
                        mapTasks.setMapLoader(mapLoader);
                        mapTasks.getTreeDownload().execute();
                    }
                } else {
                    showNoInternet();
                }
                break;
        }

        return true;
    }

    @Override
    public void plantTreeOnMap(final MarkerOptions tree) {
        MapsActivity.this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mMap.addMarker(tree);
            }
        });
    }
}


