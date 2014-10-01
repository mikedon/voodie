package com.voodie.jmx;

import org.apache.deltaspike.core.api.jmx.JmxManaged;
import org.apache.deltaspike.core.api.jmx.MBean;

import javax.enterprise.context.ApplicationScoped;

/**
 * Voodie
 * User: MikeD
 */
@ApplicationScoped
@MBean(description = "check in configuration")
public class CheckInConfiguration{

    @JmxManaged(description = "qr code url")
    private String checkInUrl = "http://localhost:8080/voodie/#/election/checkin/%s";

    public String getCheckInUrl() {
        return checkInUrl;
    }

    public void setCheckInUrl(final String checkInUrl) {
        this.checkInUrl = checkInUrl;
    }

}
