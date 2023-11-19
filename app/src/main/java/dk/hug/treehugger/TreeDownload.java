package dk.hug.treehugger;

import android.os.AsyncTask;
import android.util.Log;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import dk.hug.treehugger.core.DBhandler;
import dk.hug.treehugger.model.Root;


/**
 * Created by  Mads Fisker on 2016 - 08/03/16  13:08.
 */
public class TreeDownload extends AsyncTask<Void, Integer, Void> {
    private static final String TAG = "TreeDownload";
    private TreeDownloadCallback treeDownloadCallback;

    private boolean isDone = false;

    public TreeDownload(TreeDownloadCallback treeDownloadCallback) {
        this.treeDownloadCallback = treeDownloadCallback;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        treeDownloadCallback.downloadStart();
    }

    @Override
    protected Void doInBackground(Void... params) {
        Log.d(TAG, "doInBackground: start ");
        if (treeDownloadCallback == null || treeDownloadCallback.getContext() == null) {
            return null;
        }
        String url = "https://wfs-kbhkort.kk.dk/ows?service=wfs&version=1.0.0&request=GetFeature&typeName=k101:gadetraer&outputFormat=json&SRSNAME=EPSG:4326";
        InputStream is = null;
        Root root = null;
        publishProgress(1);
        try {
            is = new URL(url).openStream();
            ObjectMapper mapper = new ObjectMapper();
            root = mapper.readValue(is, Root.class);

            publishProgress(50);

            DBhandler.storeTreeList(root);
            publishProgress(100);
            isDone = true;
        } catch (IOException e) {
            Log.e(TAG, "Failed to download entries", e);
        }
        DBhandler.storeTreeState(1);
        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        Log.d(TAG, "doInBackground: done ");
        if (treeDownloadCallback == null) {
            return;
        }
        treeDownloadCallback.downloadEnd(isDone);

        treeDownloadCallback = null;
    }

}


