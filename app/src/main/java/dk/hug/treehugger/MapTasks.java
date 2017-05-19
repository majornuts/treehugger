package dk.hug.treehugger;

public class MapTasks {
    private TreeDownload treeDownload;
    private MapLoader mapLoader;

    public MapTasks(TreeDownload treeDownload, MapLoader mapLoader) {
        this.treeDownload = treeDownload;
        this.mapLoader = mapLoader;
    }

    public TreeDownload getTreeDownload() {
        return treeDownload;
    }

    public void setTreeDownload(TreeDownload treeDownload) {
        this.treeDownload = treeDownload;
    }

    public MapLoader getMapLoader() {
        return mapLoader;
    }

    public void setMapLoader(MapLoader mapLoader) {
        this.mapLoader = mapLoader;
    }
}
