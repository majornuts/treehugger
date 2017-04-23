package dk.hug.treehugger;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.RequiresApi;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.SparseArray;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.TileOverlayOptions;
import com.google.maps.android.heatmaps.HeatmapTileProvider;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

import dk.hug.treehugger.core.DBhandler;
import dk.hug.treehugger.model.Feature;
import dk.hug.treehugger.model.Pos;
import dk.hug.treehugger.model.Root;

public class HeatMapsActivity extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private HeatmapTileProvider mProvider;
    private ProgressDialog pd;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_heat_maps);

        MobileAds.initialize(getApplicationContext(), "ca-app-pub-3261522955094157~1151162637");

        AdView mAdView = (AdView) findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().addTestDevice(AdRequest.DEVICE_ID_EMULATOR).build();
        mAdView.loadAd(adRequest);


        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        pd = new ProgressDialog(this);
    }


    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.getUiSettings().setRotateGesturesEnabled(false);
        mMap.getUiSettings().setTiltGesturesEnabled(false);
        mMap.getUiSettings().setMyLocationButtonEnabled(true);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(60, 14), 0));

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{
                            Manifest.permission.ACCESS_FINE_LOCATION,
                            Manifest.permission.ACCESS_COARSE_LOCATION},
                    2);
        }
        if (canAccessLocation()) {
            mMap.setMyLocationEnabled(true);
        }


        LatLng dis = new LatLng(55.678814, 12.564026);
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(dis, 14));
        new MapLoader().execute();
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private boolean hasPermission(String perm) {
        return (PackageManager.PERMISSION_GRANTED == checkSelfPermission(perm));
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private boolean canAccessLocation() {
        return (hasPermission(Manifest.permission.ACCESS_FINE_LOCATION));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.input_menu, menu);
        menu.removeItem(R.id.updateTrees);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.map:
                startActivity(new Intent(this, MapsActivity.class));
                break;
            case R.id.Heatmap:

                break;
            case R.id.updateTrees:

                break;
        }

        return true;
    }

    class MapLoader extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... params) {

            ArrayList<LatLng> list = new ArrayList<>();
            for (Feature feature : DBhandler.getTrees(HeatMapsActivity.this).getFeatures()) {
                double lat = feature.getGeometry().getCoordinates().get(1);
                double lng = feature.getGeometry().getCoordinates().get(0);
                LatLng geo = new LatLng(lat, lng);
                list.add(geo);
            }
            mProvider = new HeatmapTileProvider.Builder()
                    .data(list)
                    .build();
            // Add a tile overlay to the map, using the heat map tile provider.
            HeatMapsActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    mMap.addTileOverlay(new TileOverlayOptions().tileProvider(mProvider));
                }
            });

            return null;
        }

        @Override
        protected void onPreExecute() {
            pd.setMessage("Planting trees on the map");
            pd.show();
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            pd.dismiss();
        }
    }
}
