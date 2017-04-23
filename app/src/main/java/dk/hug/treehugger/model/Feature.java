package dk.hug.treehugger.model;

/**
 * Created by  Mads Fisker on 2016 - 09/03/16  20:59.
 */

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonPropertyOrder({
        "geometry",
        "properties",
        "id"
})
public class Feature {

    @JsonProperty("geometry")
    private Geometry geometry;
    @JsonProperty("properties")
    private Properties_ properties;
    @JsonProperty("id")
    private String id;



    /**
     * @return The geometry
     */
    @JsonProperty("geometry")
    public Geometry getGeometry() {
        return geometry;
    }

    /**
     * @param geometry The geometry
     */
    @JsonProperty("geometry")
    public void setGeometry(Geometry geometry) {
        this.geometry = geometry;
    }

    /**
     * @return The properties
     */
    @JsonProperty("properties")
    public Properties_ getProperties() {
        return properties;
    }

    /**
     * @param properties The properties
     */
    @JsonProperty("properties")
    public void setProperties(Properties_ properties) {
        this.properties = properties;
    }


    /**
     * @return The id
     */
    @JsonProperty("id")
    public String getId() {
        return id;
    }

    /**
     * @param id The id
     */
    @JsonProperty("id")
    public void setId(String id) {
        this.id = id;
    }

}