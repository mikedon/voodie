package com.voodie.domain.election;

import com.voodie.domain.foodtruck.FoodTruck;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Entity
@Table(uniqueConstraints = @UniqueConstraint(columnNames = { "servingEndTime",
        "servingStartTime", "status" }))
public class Election {

    @Id
    @GeneratedValue(generator = "increment")
    @GenericGenerator(name = "increment", strategy = "increment")
    private Long id;

    private String title;

    @Enumerated(EnumType.STRING)
    private ElectionStatus status;

    private Date pollClosingDate;

    private Date pollOpeningDate;

    private Date servingStartTime;

    private Date servingEndTime;

    private Boolean allowWriteIn;

    @ManyToOne
    private FoodTruck foodTruck;

    @OneToMany(cascade = CascadeType.ALL)
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

    public FoodTruck getFoodTruck() {
        return foodTruck;
    }

    public void setFoodTruck(FoodTruck foodTruck) {
        this.foodTruck = foodTruck;
    }
}