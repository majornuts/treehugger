package dk.hug.treehugger.core;

import android.app.ProgressDialog;
import android.content.Context;

/**
 * Created by mfi on 21/02/2017.
 */

public class application {

    static ProgressDialog progressDialog;


    public  static void  startProgress(Context context){

        progressDialog = new ProgressDialog(context);
        progressDialog.setMessage("Planting trees on the map");
        progressDialog.show();

    }

    public static void stopProgress(){
        progressDialog.dismiss();
    }

}
