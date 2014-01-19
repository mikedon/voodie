package com.voodie.domain.election;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * Voodie
 * User: MikeD
 */
@Entity
@Table(uniqueConstraints =
    @UniqueConstraint(columnNames = { "longitude", "latitude", "election_id" }))
public class Candidate {

    @Id
    @GeneratedValue(generator = "increment")
    @GenericGenerator(name = "increment", strategy = "increment")
    private Long id;

    @NotNull
    @Column(nullable = false)
    private String displayName;

    private Long sortOrder;

    @NotNull
    @Column(nullable = false)
    private Double longitude;

    @NotNull
    @Column(nullable = false)
    private Double latitude;

    @OneToMany(mappedBy = "candidate")
    private List<Vote> votes;

    @ManyToOne(optional = false)
    private Election election;

    // ---------------------------------

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public List<Vote> getVotes() {
        return votes;
    }

    public void setVotes(List<Vote> votes) {
        this.votes = votes;
    }

    public Long getSortOrder() {
        return sortOrder;
    }

    public void setSortOrder(Long sortOrder) {
        this.sortOrder = sortOrder;
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

    public Election getElection() {
        return election;
    }

    public void setElection(Election election) {
        this.election = election;
    }
}
