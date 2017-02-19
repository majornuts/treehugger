package dk.hug.treehugger;

import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import com.androidmapsextensions.ClusteringSettings;
import com.androidmapsextensions.GoogleMap;
import com.androidmapsextensions.MarkerOptions;
import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.model.LatLng;

import dk.hug.treehugger.core.DBhandler;
import dk.hug.treehugger.model.Feature;
import dk.hug.treehugger.model.Pos2;
import dk.hug.treehugger.model.Root;

public class MapsActivity extends FragmentActivity  {

    private static final String TAG = "MapsActivity";
    private GoogleMap mMap;
    private Root root;
    private GoogleApiClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_maps);

        root = DBhandler.getTrees(this);
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
        mMap = ((com.androidmapsextensions.SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map)).getExtendedMap();
        mMap.getUiSettings().setRotateGesturesEnabled(false);
        mMap.getUiSettings().setTiltGesturesEnabled(false);
        mMap.getUiSettings().setMyLocationButtonEnabled(true);
        LatLng dis = new LatLng( 55.678814,12.564026);
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(dis,14));
        initMarkers();
    }


    private void initMarkers() {

        mMap.setClustering(new ClusteringSettings().enabled(true).addMarkersDynamically(true).clusterSize(75));

        for (int i = 0; i < root.getFeatures().size(); i++) {
            Feature feature = root.getFeatures().get(i);
            double lat = feature.getGeometry().getCoordinates().get(1);
            double lng = feature.getGeometry().getCoordinates().get(0);
            LatLng geo = new LatLng(lat, lng);

            Pos2 pos = new Pos2(feature.getProperties().getDanskNavn(), geo);
            MarkerOptions marker =new MarkerOptions().position(pos.getPosition()).title(pos.getName()).snippet(feature.getProperties().getTraeArt());
            mMap.addMarker(marker);
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



}


