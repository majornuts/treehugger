package dk.hug.treehugger.model;

/**
 * Created by  Mads Fisker @ Dis-Play on 2016 - 09/03/16  20:58.
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
        "coordinates"
})
public class Geometry {

    @JsonProperty("coordinates")
    private List<Double> coordinates = new ArrayList<Double>();

    /**
     * @return The coordinates
     */
    @JsonProperty("coordinates")
    public List<Double> getCoordinates() {
        return coordinates;
    }

    /**
     * @param coordinates The coordinates
     */
    @JsonProperty("coordinates")
    public void setCoordinates(List<Double> coordinates) {
        this.coordinates = coordinates;
    }

}
