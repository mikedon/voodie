package com.voodie.domain.election;

import com.voodie.domain.foodtruck.FoodTruck;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.List;

@Entity
@Table(uniqueConstraints = @UniqueConstraint(columnNames = { "servingEndTime",
        "servingStartTime", "status", "foodTruck_id" }))
public class Election {

    @Id
    @GeneratedValue(generator = "increment")
    @GenericGenerator(name = "increment", strategy = "increment")
    private Long id;

    @NotNull
    @Column(nullable = false)
    private String title;

    @Enumerated(EnumType.STRING)
    @NotNull
    @Column(nullable = false)
    private ElectionStatus status;

    @NotNull
    @Column(nullable = false)
    private Date pollClosingDate;

    @NotNull
    @Column(nullable = false)
    private Date pollOpeningDate;

    @NotNull
    @Column(nullable = false)
    private Date servingStartTime;

    @NotNull
    @Column(nullable = false)
    private Date servingEndTime;

    private Boolean allowWriteIn;

    @OneToOne
    private Candidate selectedCandidate;

    @ManyToOne(optional = false)
    @NotNull
    private FoodTruck foodTruck;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "election")
    private List<Candidate> candidates;

    // ---------------------------------

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getPollClosingDate() {
        return pollClosingDate;
    }

    public void setPollClosingDate(Date date) {
        this.pollClosingDate = date;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public ElectionStatus getStatus() {
        return status;
    }

    public void setStatus(ElectionStatus status) {
        this.status = status;
    }

    public Date getPollOpeningDate() {
        return pollOpeningDate;
    }

    public void setPollOpeningDate(Date pollOpeningDate) {
        this.pollOpeningDate = pollOpeningDate;
    }

    public Date getServingStartTime() {
        return servingStartTime;
    }

    public void setServingStartTime(Date servingStartTime) {
        this.servingStartTime = servingStartTime;
    }

    public Date getServingEndTime() {
        return servingEndTime;
    }

    public void setServingEndTime(Date servingEndTime) {
        this.servingEndTime = servingEndTime;
    }

    public Boolean getAllowWriteIn() {
        return allowWriteIn;
    }

    public void setAllowWriteIn(Boolean allowWriteIn) {
        this.allowWriteIn = allowWriteIn;
    }

    public List<Candidate> getCandidates() {
        return candidates;
    }

    public void setCandidates(List<Candidate> candidates) {
        this.candidates = candidates;
    }

    public void addCandidate(Candidate candidate){
        if(candidate != null){
            candidates.add(candidate);
            candidate.setElection(this);
        }
    }

    public FoodTruck getFoodTruck() {
        return foodTruck;
    }

    public void setFoodTruck(FoodTruck foodTruck) {
        this.foodTruck = foodTruck;
    }

    public Candidate getSelectedCandidate() {
        return selectedCandidate;
    }

    public void setSelectedCandidated(Candidate selectedCandidate) {
        this.selectedCandidate = selectedCandidate;
    }
}
