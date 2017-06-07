package dk.siit.treehugger.model;

/**
 * Created by  Mads Fisker  on 2016 - 09/03/16  20:57.
 */

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonPropertyOrder({
        "ejer",
        "saerligt_trae",
        "dansk_navn",
        "id",
        "trae_historie",
        "planteaar",
        "fredet_beskyttet_trae",
        "traeart"

})
public class Properties_ {

    @JsonProperty("ejer")
    private String ejer;
    @JsonProperty("saerligt_trae")
    private String saerligtTrae;
    @JsonProperty("dansk_navn")
    private String danskNavn;
    @JsonProperty("id")
    private Integer id;
    @JsonProperty("trae_historie")
    private String traeHistorie;
    @JsonProperty("traeart")
    private String traeArt;

    @JsonProperty("planteaar")
    private String planteaar;
    @JsonProperty("fredet_beskyttet_trae")
    private String fredetBeskyttetTrae;

    /**
     * @return The ejer
     */
    @JsonProperty("ejer")
    public String getEjer() {
        return ejer;
    }

    /**
     * @param ejer The ejer
     */
    @JsonProperty("ejer")
    public void setEjer(String ejer) {
        this.ejer = ejer;
    }

    /**
     * @return The saerligtTrae
     */
    @JsonProperty("saerligt_trae")
    public String getSaerligtTrae() {
        return saerligtTrae;
    }

    /**
     * @param saerligtTrae The saerligt_trae
     */
    @JsonProperty("saerligt_trae")
    public void setSaerligtTrae(String saerligtTrae) {
        this.saerligtTrae = saerligtTrae;
    }

    /**
     * @return The danskNavn
     */
    @JsonProperty("dansk_navn")
    public String getDanskNavn() {
        return danskNavn;
    }

    /**
     * @param danskNavn The dansk_navn
     */
    @JsonProperty("dansk_navn")
    public void setDanskNavn(String danskNavn) {
        this.danskNavn = danskNavn;
    }

    /**
     * @return The id
     */
    @JsonProperty("id")
    public Integer getId() {
        return id;
    }

    /**
     * @param id The id
     */
    @JsonProperty("id")
    public void setId(Integer id) {
        this.id = id;
    }



    /**
     * @return The traeHistorie
     */
    @JsonProperty("trae_historie")
    public String getTraeHistorie() {
        return traeHistorie;
    }

    /**
     * @param traeHistorie The trae_historie
     */
    @JsonProperty("trae_historie")
    public void setTraeHistorie(String traeHistorie) {
        this.traeHistorie = traeHistorie;
    }

    /**
     * @return The planteaar
     */
    @JsonProperty("planteaar")
    public String getPlanteaar() {
        return planteaar;
    }

    /**
     * @param planteaar The planteaar
     */
    @JsonProperty("planteaar")
    public void setPlanteaar(String planteaar) {
        this.planteaar = planteaar;
    }

    /**
     * @return The fredetBeskyttetTrae
     */
    @JsonProperty("fredet_beskyttet_trae")
    public String getFredetBeskyttetTrae() {
        return fredetBeskyttetTrae;
    }

    /**
     * @param fredetBeskyttetTrae The fredet_beskyttet_trae
     */
    @JsonProperty("fredet_beskyttet_trae")
    public void setFredetBeskyttetTrae(String fredetBeskyttetTrae) {
        this.fredetBeskyttetTrae = fredetBeskyttetTrae;
    }

    public String getTraeArt() {
        return traeArt;
    }

    public void setTraeArt(String traeArt) {
        this.traeArt = traeArt;
    }
}
