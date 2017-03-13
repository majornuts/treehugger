package dk.hug.treehugger;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.SparseArray;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

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
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        pd = new ProgressDialog(this);
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.getUiSettings().setRotateGesturesEnabled(false);
        mMap.getUiSettings().setTiltGesturesEnabled(false);
        mMap.getUiSettings().setMyLocationButtonEnabled(true);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(60,14),6));

        LatLng dis = new LatLng( 55.678814,12.564026);
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(dis,14));
        new MapLoader().execute();
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
                startActivity(new Intent(this,MapsActivity.class));
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
