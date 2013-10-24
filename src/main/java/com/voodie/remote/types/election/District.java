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
public class District extends VoodieResponse{

    private String name;

    // ---------------------------------

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
