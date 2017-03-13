package dk.hug.treehugger;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.PersistableBundle;
import android.util.Log;
import android.widget.ProgressBar;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import dk.hug.treehugger.core.DBhandler;
import dk.hug.treehugger.model.Root;


/**
 * Created by  Mads Fisker on 2016 - 08/03/16  13:08.
 */
public class TreeDownload extends AsyncTask<Void, Void, Root> {
    private static final String TAG = "TreeDownload";
    private final Context context;
    private final Handler.Callback sa;
    private long time;
    private ProgressDialog progress;

    public TreeDownload(Context context, Handler.Callback startActivity) {
        this.context = context;
        this.sa = startActivity;
    }

    @Override
    protected Root doInBackground(Void... params) {
        Log.e(TAG, "doInBackground: start ");
        String url = "http://wfs-kbhkort.kk.dk/k101/ows?service=WFS&version=1.0.0&request=GetFeature&typeName=k101:gadetraer&outputFormat=json&SRSNAME=EPSG:4326";
        InputStream is = null;
        Root root = null;
        try {
            is = new URL(url).openStream();

            ObjectMapper mapper = new ObjectMapper();

            root = mapper.readValue(is, Root.class);

        } catch (JsonGenerationException e) {
            e.printStackTrace();
        } catch (JsonParseException e) {
            e.printStackTrace();
        } catch (JsonMappingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return root;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        time = System.currentTimeMillis();
        progress = new ProgressDialog(context);
        progress.setMessage("downloading trees");
        progress.show();

    }

    @Override
    protected void onPostExecute(Root root) {
        DBhandler.storeTrees(context, root);
        DBhandler.storeTreeState(context, 1);
        Log.e(TAG, "onPostExecute:download time:" + (System.currentTimeMillis() - time));
        progress.dismiss();

        Bundle b = new Bundle();
        b.putBoolean("isDone", true);
        if (root == null) {
            b.putBoolean("isDone", false);
        }
        Message m = new Message();
        m.setData(b);
        sa.handleMessage(m);

    }

}


