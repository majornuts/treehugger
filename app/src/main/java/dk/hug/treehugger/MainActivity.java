package dk.hug.treehugger;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import butterknife.ButterKnife;
import butterknife.OnClick;
import dk.hug.treehugger.core.DBhandler;
import dk.hug.treehugger.core.application;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        DBhandler.storeTreeState(this,0);
    }

    @OnClick(R.id.download)
    void download() {
        new TreeDownload(this).execute();
    }


    TreeDownload td = new TreeDownload(this);

    @OnClick(R.id.maps)
    void maps() {
        Log.e(TAG, "maps: "+DBhandler.getTreeState(this));
        if (DBhandler.getTreeState(this) != 1) {//&& td.getStatus() != null){ //todo lookup getstatus, skip if running.
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("do you want to download trees?")
                    .setNegativeButton("no", null)
                    .setPositiveButton("yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            td.execute();
                        }
                    }).show();
        } else {
            application.startProgress(this);
            startActivity(new Intent(this, MapsActivity.class));
        }
    }

    @OnClick(R.id.heatmap)
    void heatmap() {

        if (DBhandler.getTreeState(this) != 0) {//&& td.getStatus() != null){ //todo lookup getstatus, skip if running.
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("do you want to download trees?")
                    .setNegativeButton("no", null)
                    .setPositiveButton("yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            td.execute();
                        }
                    });
        } else {
            application.startProgress(this);
            startActivity(new Intent(this, HeatMapsActivity.class));

        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

    }
}
