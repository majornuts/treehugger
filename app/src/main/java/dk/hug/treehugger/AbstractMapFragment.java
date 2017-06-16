package dk.hug.treehugger;

import android.*;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.design.widget.Snackbar;
import android.view.View;

import com.google.android.gms.maps.GoogleMap;

public abstract class AbstractMapFragment extends Fragment {
    protected TreeDownload treeDownload;
    protected GoogleMap mMap;
    protected ProgressDialog progressDialog;
    protected View view;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(savedInstanceState==null) {
            setRetainInstance(true);
        }
    }

    @Override
    public void onDetach() {
        if(progressDialog!=null&&progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
        super.onDetach();
    }


    @RequiresApi(api = Build.VERSION_CODES.M)
    private boolean hasPermission(String perm) {
        return (PackageManager.PERMISSION_GRANTED == getActivity().checkSelfPermission(perm));
    }

    protected void showNoInternet() {
        Snackbar.make(view, R.string.no_internet, Snackbar.LENGTH_LONG).show();
    }

    protected boolean canAccessLocation() {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            return (hasPermission(android.Manifest.permission.ACCESS_FINE_LOCATION));
        } else {
            return true;
        }
    }

    protected boolean checkConnectivity() {
        ConnectivityManager cm =
                (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        return activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();
    }

    protected void setupMap() {
        mMap.getUiSettings().setRotateGesturesEnabled(false);
        mMap.getUiSettings().setTiltGesturesEnabled(false);
        mMap.getUiSettings().setMyLocationButtonEnabled(true);
    }
}
