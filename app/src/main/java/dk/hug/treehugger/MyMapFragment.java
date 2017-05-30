package dk.hug.treehugger;

import android.Manifest;
import android.app.DialogFragment;
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

import com.androidmapsextensions.ClusterGroup;
import com.androidmapsextensions.ClusterOptions;
import com.androidmapsextensions.ClusterOptionsProvider;
import com.androidmapsextensions.ClusteringSettings;
import com.androidmapsextensions.Marker;
import com.androidmapsextensions.MarkerOptions;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.clustering.ClusterItem;

import java.util.List;

import dk.hug.treehugger.core.DBhandler;
import dk.hug.treehugger.utils.CustomClusterOptionsProvider;


public class MyMapFragment extends Fragment implements MapLoaderCallback {
    private static final String TAG = "MyMapFragment";
    private com.androidmapsextensions.GoogleMap mMap;
    private View view;
    private MapLoader mapLoader;

    private static final int DYNAMIC_GROUP = ClusterGroup.FIRST_USER;

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
        getActivity().setTitle("Map");
        view = inflater.inflate(R.layout.fragment_map, container, false);

        MobileAds.initialize(this.getActivity(), this.getResources().getString(R.string.unit_id));

        AdView mAdView = (AdView) view.findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().addTestDevice(AdRequest.DEVICE_ID_EMULATOR).build();
        mAdView.loadAd(adRequest);
        initmap();

        mapLoader = new MapLoader(getActivity(), this);

        if (DBhandler.getTreeState(getActivity()) != 1) {
            if (checkConnectivity()) {
//                new TreeDownload(getContext(), new Handler.Callback() {
//                    @Override
//                    public boolean handleMessage(Message msg) {
//                        if (msg.getData().getBoolean("isDone")) {
//
//                        }
//                        return false;
//                    }
//                }, this);


                new TreeDownload(getActivity(), new Handler.Callback() {
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
    private void initmap() {
        mMap = ((com.androidmapsextensions.MapFragment) getChildFragmentManager().findFragmentById(R.id.map)).getExtendedMap();

        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(), new String[]{
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

        mMap.setClustering(new ClusteringSettings().enabled(true).addMarkersDynamically(true).clusterSize(75).clusterOptionsProvider(new CustomClusterOptionsProvider(getResources())));
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private boolean hasPermission(String perm) {
        return (PackageManager.PERMISSION_GRANTED == getActivity().checkSelfPermission(perm));
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private boolean canAccessLocation() {
        return (hasPermission(Manifest.permission.ACCESS_FINE_LOCATION));
    }


    @Override
    public void plantTreeOnMap(final MarkerOptions tree) {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {

                mMap.addMarker(tree);
            }
        });
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

}
