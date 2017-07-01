package dk.hug.treehugger;

import android.Manifest;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.maps.*;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.TileOverlayOptions;

import dk.hug.treehugger.core.DBhandler;


public class HeatMapFragment extends AbstractMapFragment implements OnMapReadyCallback, HeatMapLoaderCallback, TreeDownloadCallback {
    private HeatMapLoader mapLoader;

    public HeatMapFragment() {

    }

    public static HeatMapFragment newInstance() {
        HeatMapFragment fragment = new HeatMapFragment();
        fragment.setRetainInstance(true);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        getActivity().setTitle(getString(R.string.title_activity_heat_maps));

        view = inflater.inflate(R.layout.fragment_heat_map, container, false);

        FragmentManager fm = getFragmentManager();

        boolean refresh = true;
        if(savedInstanceState!=null)
            refresh=false;

        MapFragment fr = (MapFragment) fm.findFragmentById(R.id.mapview);
        if(fr==null||refresh) {
            fr = MapFragment.newInstance();
            fm.beginTransaction().replace(R.id.mapview, fr).commit();
            moveCamera = true;
        } else {
            moveCamera = false;
        }

        MobileAds.initialize(this.getActivity(), this.getResources().getString(R.string.unit_id));
        AdView mAdView = (AdView) view.findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().addTestDevice(AdRequest.DEVICE_ID_EMULATOR).build();
        mAdView.loadAd(adRequest);

        fr.getMapAsync(this);
        return view;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        if(moveCamera) {
            LatLng dis = new LatLng(55.678814, 12.564026);
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(dis, 14));
            moveCamera = false;
        }

        setupMap();

        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(), new String[]{
                            Manifest.permission.ACCESS_FINE_LOCATION,
                            Manifest.permission.ACCESS_COARSE_LOCATION},
                    2);
        }
        if (canAccessLocation()) {
            mMap.setMyLocationEnabled(true);
        }

        mMap.setOnMapLoadedCallback(new GoogleMap.OnMapLoadedCallback() {
            @Override
            public void onMapLoaded() {
                mapLoader = new HeatMapLoader(HeatMapFragment.this.getActivity(), HeatMapFragment.this);

                if (DBhandler.getTreeState(getActivity()) != 1) {
                    if (checkConnectivity()) {
                        treeDownload = new TreeDownload(getActivity(), new Handler.Callback() {
                            @Override
                            public boolean handleMessage(Message msg) {
                                mapLoader = new HeatMapLoader(HeatMapFragment.this.getActivity(), HeatMapFragment.this);
                                mMap.clear();
                                mapLoader.execute();
                                return true;
                            }
                        }, HeatMapFragment.this);
                        treeDownload.execute();
                    } else {
                        showNoInternet();
                    }
                } else {
                    mapLoader.execute();
                }
            }
        });
    }

    @Override
    public void updateHeatMap(final TileOverlayOptions overlayOptions) {
        // Add a tile overlay to the map, using the heat map tile provider.
        mMap.addTileOverlay(overlayOptions);
    }

    @Override
    public void startLoader() {
        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage(getString(R.string.planting_trees));
        progressDialog.setCancelable(false);
        progressDialog.show();
    }

    @Override
    public void stopLoader() {
        progressDialog.dismiss();
    }

    @Override
    public void downloadStart() {
        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage(getString(R.string.downloading_trees));
        progressDialog.setCancelable(false);
        progressDialog.show();
    }

    @Override
    public void downloadEnd() {
        progressDialog.dismiss();
    }
}
