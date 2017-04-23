package dk.hug.treehugger.model;

import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.clustering.ClusterItem;

/**
 * Created by  Mads Fisker  on 2016 - 10/03/16  10:17.
 */
public class Pos implements ClusterItem {

    private String name;
    private LatLng position;

    public Pos(String name, LatLng position) {
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
