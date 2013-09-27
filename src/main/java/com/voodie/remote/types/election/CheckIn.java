package com.voodie.remote.types.election;

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
public class CheckIn {

    private Long election;

    // ---------------------------------

    public Long getElection() {
        return election;
    }

    public void setElection(Long election) {
        this.election = election;
    }

}
