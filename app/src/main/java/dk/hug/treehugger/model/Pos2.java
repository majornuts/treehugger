package dk.hug.treehugger.model;

import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.clustering.ClusterItem;

import io.nlopez.clusterer.Clusterable;

/**
 * Created by  Mads Fisker @ Dis-Play on 2016 - 10/03/16  10:17.
 */
public class Pos2 implements ClusterItem {

    private String name;
    private LatLng position;

    public Pos2(String name, LatLng position) {
        this.name = name;
        this.position = position;
    }

    public String getName() {
        return name;
    }

    @Override
    public LatLng getPosition() {
        return position;
    }


}
