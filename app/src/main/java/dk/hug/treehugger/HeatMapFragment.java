package dk.hug.treehugger;

import android.Manifest;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
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
import com.google.android.gms.maps.*;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.TileOverlayOptions;

import dk.hug.treehugger.core.DBhandler;


public class HeatMapFragment extends Fragment implements OnMapReadyCallback, HeatMapLoaderCallback {

    private GoogleMap mMap;
    private HeatMapLoader mapLoader;
    private View view;

    public HeatMapFragment() {

    }

    public static HeatMapFragment newInstance() {
        HeatMapFragment fragment = new HeatMapFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_heat_map, container, false);

        getActivity().setTitle("heat map");
        MobileAds.initialize(this.getActivity(), this.getResources().getString(R.string.unit_id));

        AdView mAdView = (AdView) view.findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().addTestDevice(AdRequest.DEVICE_ID_EMULATOR).build();
        mAdView.loadAd(adRequest);

        mapLoader = new HeatMapLoader(getContext(), this);

        getMapFragment().getMapAsync(this);

        if (DBhandler.getTreeState(getActivity()) != 1) {
            if (checkConnectivity()) {
                new TreeDownload(getContext(), new Handler.Callback() {
                    @Override
                    public boolean handleMessage(Message msg) {
                        return false;
                    }
                }).execute();
            } else {
                showNoInternet();
            }
        } else {
            mapLoader.execute();
        }
        return view;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private boolean hasPermission(String perm) {
        return (PackageManager.PERMISSION_GRANTED == getActivity().checkSelfPermission(perm));
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private boolean canAccessLocation() {
        return (hasPermission(Manifest.permission.ACCESS_FINE_LOCATION));
    }

    private boolean checkConnectivity() {
        ConnectivityManager cm =
                (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        return activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();
    }

    private void showNoInternet() {
        Snackbar.make(view, R.string.no_internet, Snackbar.LENGTH_LONG).show();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.getUiSettings().setRotateGesturesEnabled(false);
        mMap.getUiSettings().setTiltGesturesEnabled(false);
        mMap.getUiSettings().setMyLocationButtonEnabled(true);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(60, 14), 0));

        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(), new String[]{
                            Manifest.permission.ACCESS_FINE_LOCATION,
                            Manifest.permission.ACCESS_COARSE_LOCATION},
                    2);
        }
        if (canAccessLocation()) {
            mMap.setMyLocationEnabled(true);
        }

        LatLng dis = new LatLng(55.678814, 12.564026);

        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(dis, 14));

        if (DBhandler.getTreeState(getActivity()) == 1) {
            mapLoader = new HeatMapLoader(getActivity(), this);
            mapLoader.execute();
        }

    }

    @Override
    public void updateHeatMap(final TileOverlayOptions overlayOptions) {
        // Add a tile overlay to the map, using the heat map tile provider.
//                mMap.addTileOverlay(overlayOptions);
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mMap.addTileOverlay(overlayOptions);
            }
        });


    }

    private static final String TAG = "HeatMapFragment";

    private MapFragment getMapFragment() {
        FragmentManager fm = null;

        Log.d(TAG, "sdk: " + Build.VERSION.SDK_INT);
        Log.d(TAG, "release: " + Build.VERSION.RELEASE);

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            Log.d(TAG, "using getFragmentManager");
            fm = getFragmentManager();
        } else {
            Log.d(TAG, "using getChildFragmentManager");
            fm = getChildFragmentManager();
        }

        return (MapFragment) fm.findFragmentById(R.id.map);
    }

}
