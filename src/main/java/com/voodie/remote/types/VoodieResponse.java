package com.voodie.remote.types;

import com.google.common.collect.Lists;

import java.util.List;

/**
 * Voodie
 * User: MikeD
 */
public class VoodieResponse {
    private List<Alert> alerts = Lists.newArrayList();

    public List<Alert> getAlerts() {
        return alerts;
    }

    public void setAlerts(List<Alert> alerts) {
        this.alerts = alerts;
    }
}
