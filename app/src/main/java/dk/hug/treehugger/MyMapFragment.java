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


public class MyMapFragment extends Fragment implements OnMapReadyCallback, GoogleMap.OnCameraMoveListener {
    private static final String TAG = "MyMapFragment";
    private View view;
    private MapLoader mapLoader;


    private ClusterManager<Pos> mClusterManager;
    private GoogleMap mMap;

    //todo https://androidresearch.wordpress.com/2013/05/10/dealing-with-asynctask-and-screen-orientation/   locked som portret mode in til nu.
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
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        getActivity().setTitle(getString(R.string.title_activity_maps));
        view = inflater.inflate(R.layout.fragment_map, container, false);

        MobileAds.initialize(this.getActivity(), this.getResources().getString(R.string.unit_id));

        AdView mAdView = (AdView) view.findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().addTestDevice(AdRequest.DEVICE_ID_EMULATOR).build();
        mAdView.loadAd(adRequest);
        getMapFragment().getMapAsync(this);

        return view;

    }


    @RequiresApi(api = Build.VERSION_CODES.M)
    private boolean hasPermission(String perm) {
        return (PackageManager.PERMISSION_GRANTED == getActivity().checkSelfPermission(perm));
    }


    private boolean canAccessLocation() {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            return (hasPermission(Manifest.permission.ACCESS_FINE_LOCATION));
        } else {
            return true;
        }
    }


    private void showNoInternet() {
        Snackbar.make(view, R.string.no_internet, Snackbar.LENGTH_LONG).show();
    }

    private boolean checkConnectivity() {
        ConnectivityManager cm =
                (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        return activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();
    }

    @Override
    public void onMapReady(final GoogleMap googleMap) {

        googleMap.setOnCameraMoveListener(this);

        LatLng dis = new LatLng(55.678814, 12.564026);
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(dis, 14));
        googleMap.setOnMapLoadedCallback(new GoogleMap.OnMapLoadedCallback() {
            @Override
            public void onMapLoaded() {
                mClusterManager = new ClusterManager<Pos>(getActivity(), googleMap);
                mClusterManager.setRenderer(new PosClusterRenderer(getActivity(), googleMap, mClusterManager));
                mMap = googleMap;
                mapLoader = new MapLoader(getActivity(), mClusterManager, googleMap.getProjection());
                if (DBhandler.getTreeState(getActivity()) != 1) {
                    if (checkConnectivity()) {
                        new TreeDownload(getActivity(), new Handler.Callback() {
                            @Override
                            public boolean handleMessage(Message msg) {
                                mapLoader.execute();
                                return false;
                            }
                        }).execute();
                    } else {
                        showNoInternet();
                    }
                } else {
                    mapLoader.execute();
                }

                if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(getActivity(), new String[]{
                                    Manifest.permission.ACCESS_FINE_LOCATION,
                                    Manifest.permission.ACCESS_COARSE_LOCATION},
                            2);
                }
                if (canAccessLocation()) {
                    googleMap.setMyLocationEnabled(true);
                }
            }
        });

        googleMap.getUiSettings().setRotateGesturesEnabled(false);
        googleMap.getUiSettings().setTiltGesturesEnabled(false);
        googleMap.getUiSettings().setMyLocationButtonEnabled(true);
        googleMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                marker.showInfoWindow();
                return true;
            }
        });
    }


    private MapFragment getMapFragment() {
        FragmentManager fm = null;
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            fm = getFragmentManager();
        } else {
            fm = getChildFragmentManager();
        }
        return (MapFragment) fm.findFragmentById(R.id.map);
    }


    @Override
    public void onCameraMove() {
        mapLoader = new MapLoader(getActivity(), mClusterManager, mMap.getProjection());
        mapLoader.execute();
    }
}
