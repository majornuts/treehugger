package dk.hug.treehugger;

import android.app.Fragment;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.google.android.gms.maps.GoogleMap;

public abstract class AbstractMapFragment extends Fragment {
    protected TreeDownload treeDownload;
    protected GoogleMap mMap;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(savedInstanceState==null) {
            setRetainInstance(true);
        } else {
            if(treeDownload!=null&&treeDownload.getStatus()== AsyncTask.Status.RUNNING) {
                treeDownload.cancel(true);
            }
        }
    }
}
