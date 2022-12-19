package dk.hug.treehugger;

import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import com.google.android.material.snackbar.Snackbar;

import android.view.View;

import com.google.android.gms.maps.GoogleMap;

public abstract class AbstractMapFragment extends Fragment {
    protected TreeDownload treeDownload;
    protected GoogleMap mMap;
    protected ProgressDialog progressDialog;
    protected View view;
    protected boolean moveCamera;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (savedInstanceState == null) {
            setRetainInstance(true);
        }
    }

    @Override
    public void onDetach() {
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
        super.onDetach();
    }


    private boolean hasPermission(String perm) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            return (PackageManager.PERMISSION_GRANTED == getActivity().checkSelfPermission(perm));
        }
        return true;
    }

    protected void showNoInternet() {
        Snackbar.make(view, R.string.no_internet, Snackbar.LENGTH_LONG).show();
    }

    protected boolean canAccessLocation() {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            return (hasPermission(android.Manifest.permission.ACCESS_FINE_LOCATION));
        } else {
            return true;
        }
    }

    protected boolean checkConnectivity() {
        ConnectivityManager cm = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        return activeNetwork != null && activeNetwork.isConnectedOrConnecting();
    }

    protected void setupMap() {
        mMap.getUiSettings().setRotateGesturesEnabled(false);
        mMap.getUiSettings().setTiltGesturesEnabled(false);
        mMap.getUiSettings().setMyLocationButtonEnabled(true);
    }
}
