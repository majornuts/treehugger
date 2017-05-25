package dk.hug.treehugger;

public interface TreeDownloadCallback {
    void updateDownloadProgress(String progressMessage);
    void updateDownloadComplete(boolean failed);
}
