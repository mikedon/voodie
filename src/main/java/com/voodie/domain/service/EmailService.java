package com.voodie.domain.service;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientRequest;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.filter.HTTPBasicAuthFilter;
import com.sun.jersey.api.client.filter.LoggingFilter;
import com.sun.jersey.api.representation.Form;
import com.voodie.domain.foodie.Foodie;
import com.voodie.domain.foodtruck.FoodTruck;
import com.voodie.jmx.EmailConfiguration;

import javax.ejb.Asynchronous;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;


/**
 * Voodie
 * User: MikeD
 */
@Stateless
@Asynchronous
public class EmailService {

    @Inject
    protected EmailConfiguration emailConfiguration;

    public void sendFoodieRegistrationEmail(Foodie foodie){
        Form form = new Form();
        form.add("to", foodie.getUser().getEmailAddress());
        form.add("subject", "Welcome To Voodie!");
        form.add("text", "Hey " + foodie.getUser().getFirstName() + ".  Thanks for registering!");
        sendEmail(form);
    }

    public void sendFoodTruckRegistrationEmail(FoodTruck foodTruck){
        Form form = new Form();
        form.add("to", foodTruck.getUser().getEmailAddress());
        form.add("subject", "Welcome To Voodie!");
        form.add("text", "Hey " + foodTruck.getUser().getFirstName() + ".  Thanks for registering!");
        sendEmail(form);
    }

    protected void sendEmail(Form form){
        form.add("from", emailConfiguration.getFrom());
        Client client = Client.create();
        client.addFilter(new HTTPBasicAuthFilter("api", emailConfiguration.getApiKey()));
        client.addFilter(new LoggingFilter(System.out));
        WebResource webResource = client
                .resource("https://api.mailgun.net/v2/voodie.mailgun.org/messages");
        ClientResponse response = webResource.accept(MediaType.APPLICATION_FORM_URLENCODED_TYPE)
                .post(ClientResponse.class, form);
    }
}
