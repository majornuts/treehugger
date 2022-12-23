package dk.hug.treehugger;

import android.content.Context;

import java.util.List;

import dk.hug.treehugger.model.Pos;

public interface MapLoaderCallback {
    Context getActivityContext();

    void updateMap(List<Pos> list);
}
