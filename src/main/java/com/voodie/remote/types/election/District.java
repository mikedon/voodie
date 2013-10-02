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
public class District {

    private String name;

    // ---------------------------------

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
