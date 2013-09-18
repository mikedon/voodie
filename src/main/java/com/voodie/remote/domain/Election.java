package com.voodie.remote.domain;

import com.google.common.collect.Lists;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.Date;
import java.util.List;

@SuppressWarnings("serial")
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class Election {
    private Long id;
    private String title;
    private Date servingStartTime;
    private Date servingEndTime;
    private Date pollOpeningDate;
    private Date pollClosingDate;
    private Boolean allowWriteIn;
    private List<Candidate> candidates = Lists.newArrayList();

    // ---------------------------------

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<Candidate> getCandidates() {
        return candidates;
    }

    public void setCandidates(List<Candidate> candidates) {
        this.candidates = candidates;
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

    public Date getPollOpeningDate() {
        return pollOpeningDate;
    }

    public void setPollOpeningDate(Date pollOpeningDate) {
        this.pollOpeningDate = pollOpeningDate;
    }

    public Date getPollClosingDate() {
        return pollClosingDate;
    }

    public void setPollClosingDate(Date pollClosingDate) {
        this.pollClosingDate = pollClosingDate;
    }

    public Boolean getAllowWriteIn() {
        return allowWriteIn;
    }

    public void setAllowWriteIn(Boolean allowWriteIn) {
        this.allowWriteIn = allowWriteIn;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
