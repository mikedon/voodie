package com.voodie.jmx;

import org.apache.deltaspike.core.api.jmx.JmxManaged;
import org.apache.deltaspike.core.api.jmx.MBean;

import javax.enterprise.context.ApplicationScoped;

/**
 * Voodie
 * User: MikeD
 */
@ApplicationScoped
@MBean(description = "email configuration")
public class EmailConfiguration {

    @JmxManaged(description = "from field in email")
    private String from = "support@voodie.co";

    @JmxManaged(description = "third party email service key")
    private String apiKey = "key-6ij6m-cczmpc4zx61ihshrvvcc70cik5";

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getApiKey() {
        return apiKey;
    }

    public void setApiKey(String apiKey) {
        this.apiKey = apiKey;
    }
}
