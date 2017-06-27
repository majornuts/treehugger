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
import android.support.annotation.RequiresApi;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.maps.android.clustering.ClusterManager;

import dk.hug.treehugger.core.DBhandler;
import dk.hug.treehugger.model.Pos;


public class MyMapFragment extends AbstractMapFragment implements OnMapReadyCallback, GoogleMap.OnCameraMoveListener, TreeDownloadCallback {
    private static final String TAG = "MyMapFragment";
    private MapLoader mapLoader;

    private ClusterManager<Pos> mClusterManager;

    public MyMapFragment() {
        // Required empty public constructor
    }

    public static MyMapFragment newInstance() {
        MyMapFragment fragment = new MyMapFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        getActivity().setTitle(getString(R.string.title_activity_maps));
        view = inflater.inflate(R.layout.fragment_map, container, false);

        FragmentManager fm = getFragmentManager();

        boolean refresh = true;
        if(savedInstanceState!=null)
            refresh=false;

        MapFragment fr = (MapFragment) fm.findFragmentById(R.id.map);
        if(fr==null||refresh) {
            fr = MapFragment.newInstance();
            fm.beginTransaction().replace(R.id.map, fr).commit();
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
    public void onMapReady(final GoogleMap googleMap) {
        mMap = googleMap;

        if(moveCamera) {
            LatLng dis = new LatLng(55.678814, 12.564026);
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(dis, 14));
            moveCamera = false;
        }

        setupMap();

        mMap.setOnCameraMoveListener(this);
        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(), new String[]{
                            Manifest.permission.ACCESS_FINE_LOCATION,
                            Manifest.permission.ACCESS_COARSE_LOCATION},
                    2);
        }
        if (canAccessLocation()) {
            mMap.setMyLocationEnabled(true);
        }

        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                marker.showInfoWindow();
                return true;
            }
        });

        mMap.setOnMapLoadedCallback(new GoogleMap.OnMapLoadedCallback() {
            @Override
            public void onMapLoaded() {
                mClusterManager = new ClusterManager<Pos>(getActivity(), googleMap);
                mClusterManager.setRenderer(new PosClusterRenderer(getActivity(), googleMap, mClusterManager));
                mMap = googleMap;
                mapLoader = new MapLoader(getActivity(), mClusterManager, googleMap.getProjection());
                if (DBhandler.getTreeState(getActivity()) != 1) {
                    if (checkConnectivity()) {
                        treeDownload = new TreeDownload(getActivity(), new Handler.Callback() {
                            @Override
                            public boolean handleMessage(Message msg) {
                                mapLoader = new MapLoader(getActivity(), mClusterManager, googleMap.getProjection());
                                mMap.clear();
                                mapLoader.execute();
                                return true;
                            }
                        }, MyMapFragment.this);
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
    public void onStop() {
        super.onStop();
        DBhandler.closeDB();
    }

    @Override
    public void onCameraMove() {
        mapLoader = new MapLoader(getActivity(), mClusterManager, mMap.getProjection());
        mapLoader.execute();
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
