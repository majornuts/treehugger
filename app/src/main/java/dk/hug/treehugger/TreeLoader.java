package dk.hug.treehugger;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import dk.hug.treehugger.core.DBhandler;
import dk.hug.treehugger.model.Root;

/**
 * Created by  Mads Fisker @ Dis-Play on 2016 - 09/03/16  22:23.
 */
public class TreeLoader extends AsyncTask<Void,Void,Root> {
    private final Context context;
    OnTaskFinishedListener mListener;


    public TreeLoader(Context context) {
        this.context = context;
    }

    @Override
    protected Root doInBackground(Void... params) {
        Log.d("", "doInBackground: ");
        return DBhandler.getTrees(context);
    }

    @Override
    protected void onPostExecute(Root root) {
            mListener.onFinished(root);
    }

    public void setOnTaskFinishedListener(OnTaskFinishedListener listener) {
        mListener = listener;
    }


    public interface OnTaskFinishedListener {
        public void onFinished(Root root);
    }
}
