package dk.hug.treehugger;

public interface TreeDownloadCallback {
    void updateDownloadProgress(int progress);
    void updateDownloadComplete(boolean failed);
}
