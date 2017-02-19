package dk.hug.treehugger;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

    }





    @OnClick(R.id.download)
    void download() {
        new TreeDownload(this).execute();

    }


    @OnClick(R.id.maps)
    void maps() {
        startActivity(new Intent(this, MapsActivity.class));
    }

    @OnClick(R.id.heatmap)
    void heatmap() {
        startActivity(new Intent(this, HeatMapsActivity.class));

    }


}
