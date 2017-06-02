package dk.hug.treehugger.model;

import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.clustering.ClusterItem;

/**
 * Created by  Mads Fisker  on 2016 - 10/03/16  10:17.
 */
public class Pos implements ClusterItem {

    private String name;
    private String latname;
    private LatLng position;

    public Pos(String danishName, String latname, LatLng position) {
        this.name = danishName;
        this.latname = latname;
        this.position = position;
    }

    public Pos (double lat,double lng){
        this.position = new LatLng(lat,lng);
    }


    public String getName() {
        return name;
    }

    @Override
    public LatLng getPosition() {
        return position;
    }

    @Override
    public String getTitle() {
        return name;
    }

    @Override
    public String getSnippet() {
        return latname;
    }


}
