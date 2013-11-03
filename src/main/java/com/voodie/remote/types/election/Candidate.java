package com.voodie.remote.types.election;

import com.voodie.remote.types.VoodieResponse;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Voodie
 * User: MikeD
 */
@SuppressWarnings("serial")
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class Candidate extends VoodieResponse{

    private Long id;

    private String displayName;

    private Double longitude;

    private Double latitude;

    private Long numberOfVotes = 0L;

    private Double percentageOfVotes = 0D;

    // ---------------------------------

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getNumberOfVotes() {
        return numberOfVotes;
    }

    public void setNumberOfVotes(Long numberOfVotes) {
        this.numberOfVotes = numberOfVotes;
    }

    public Double getPercentageOfVotes() {
        return percentageOfVotes;
    }

    public void setPercentageOfVotes(Double percentageOfVotes) {
        this.percentageOfVotes = percentageOfVotes;
    }
}
