package dk.hug.treehugger.model;
/**
 * Created by  Mads Fisker @ Dis-Play on 2016 - 09/03/16  21:00.
 */

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;


@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonPropertyOrder({
        "crs",
        "type",
        "features"
})


public class Root {

    @JsonProperty("crs")
    private Crs crs;
    @JsonProperty("type")
    private String type;
    @JsonProperty("features")
    private List<Feature> features = new ArrayList<Feature>();


    /**
     * @return The crs
     */
    @JsonProperty("crs")
    public Crs getCrs() {
        return crs;
    }

    /**
     * @param crs The crs
     */
    @JsonProperty("crs")
    public void setCrs(Crs crs) {
        this.crs = crs;
    }

    /**
     * @return The type
     */
    @JsonProperty("type")
    public String getType() {
        return type;
    }

    /**
     * @param type The type
     */
    @JsonProperty("type")
    public void setType(String type) {
        this.type = type;
    }

    /**
     * @return The features
     */
    @JsonProperty("features")
    public List<Feature> getFeatures() {
        return features;
    }

    /**
     * @param features The features
     */
    @JsonProperty("features")
    public void setFeatures(List<Feature> features) {
        this.features = features;
    }

}
