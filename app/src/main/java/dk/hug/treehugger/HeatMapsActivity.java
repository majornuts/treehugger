package dk.hug.treehugger;

import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.SparseArray;
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
import dk.hug.treehugger.model.Root;

public class HeatMapsActivity extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private HeatmapTileProvider mProvider;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_heat_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.getUiSettings().setRotateGesturesEnabled(false);
        mMap.getUiSettings().setTiltGesturesEnabled(false);
        mMap.getUiSettings().setMyLocationButtonEnabled(true);
        mMap.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(60,14)));

        LatLng dis = new LatLng( 55.678814,12.564026);
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(dis,10));
        addHeatMap();
    }

    private void addHeatMap() {

        ArrayList<LatLng> list = new ArrayList<>();
        for (Feature feature : DBhandler.getTrees(this).getFeatures()) {
            double lat = feature.getGeometry().getCoordinates().get(1);
            double lng = feature.getGeometry().getCoordinates().get(0);
            LatLng geo = new LatLng(lat, lng);
            list.add(geo);
        }
        mProvider = new HeatmapTileProvider.Builder()
                .data(list)
                .build();
        // Add a tile overlay to the map, using the heat map tile provider.
        mMap.addTileOverlay(new TileOverlayOptions().tileProvider(mProvider));
    }
}
