package com.voodie.remote.types.election;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;

@SuppressWarnings("serial")
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class Vote implements Serializable {

    private Long candidate;

    public Long getCandidate() {
        return candidate;
    }

    public void setCandidate(Long candidate) {
        this.candidate = candidate;
    }
}