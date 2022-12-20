package dk.hug.treehugger;

import android.content.Context;

public interface TreeDownloadCallback {
    Context getContext();
    void downloadStart();
    void downloadEnd(boolean isDone);
}
