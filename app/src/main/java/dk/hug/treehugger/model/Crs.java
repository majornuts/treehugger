package dk.hug.treehugger.model;

/**
 * Created by  Mads Fisker @ Dis-Play on 2016 - 09/03/16  20:53.
 */
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonPropertyOrder({
        "type",
        "properties"
})

public class Crs {

    @JsonProperty("type")
    private String type;
    @JsonProperty("properties")
    private Properties properties;

    /**
     *
     * @return
     * The type
     */
    @JsonProperty("type")
    public String getType() {
        return type;
    }

    /**
     *
     * @param type
     * The type
     */
    @JsonProperty("type")
    public void setType(String type) {
        this.type = type;
    }

    /**
     *
     * @return
     * The properties
     */
    @JsonProperty("properties")
    public Properties getProperties() {
        return properties;
    }

    /**
     *
     * @param properties
     * The properties
     */
    @JsonProperty("properties")
    public void setProperties(Properties properties) {
        this.properties = properties;
    }
}