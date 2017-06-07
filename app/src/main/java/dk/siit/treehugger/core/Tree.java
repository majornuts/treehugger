package dk.siit.treehugger.core;

public class Tree {
    private String species;
    private String danishName;
    private double lat;
    private double lon;

    public Tree(String species, String danishName, double lat, double lon) {
        this.species = species;
        this.danishName = danishName;
        this.lat = lat;
        this.lon = lon;
    }

    public String getSpecies() {
        return species;
    }

    public void setSpecies(String species) {
        this.species = species;
    }

    public String getDanishName() {
        return danishName;
    }

    public void setDanishName(String danishName) {
        this.danishName = danishName;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLon() {
        return lon;
    }

    public void setLon(double lon) {
        this.lon = lon;
    }
}
