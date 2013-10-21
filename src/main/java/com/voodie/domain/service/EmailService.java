package com.voodie.domain.service;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.filter.HTTPBasicAuthFilter;
import com.sun.jersey.api.client.filter.LoggingFilter;
import com.sun.jersey.api.representation.Form;
import com.voodie.domain.foodie.Foodie;
import com.voodie.domain.foodtruck.FoodTruck;

import javax.ejb.Stateless;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;


/**
 * Voodie
 * User: MikeD
 */
@Stateless
public class EmailService {

    private static final String API_KEY = "key-6ij6m-cczmpc4zx61ihshrvvcc70cik5";

    public void sendFoodieRegistrationEmail(Foodie foodie){
        Client client = Client.create();
        client.addFilter(new HTTPBasicAuthFilter("api", API_KEY));
        client.addFilter(new LoggingFilter(System.out));
        WebResource webResource = client
                .resource("https://api.mailgun.net/v2/voodie.mailgun.org/messages");
        Form form = new Form();
        form.add("to", foodie.getUser().getEmailAddress());
        form.add("from", "test@voodie.co");
        form.add("subject", "Welcome To Voodie!");
        form.add("text", "Hey " + foodie.getUser().getFirstName() + ".  Thanks for registering!");
        ClientResponse response = webResource.accept(MediaType.APPLICATION_FORM_URLENCODED_TYPE)
                .post(ClientResponse.class, form);
        System.out.println(response);
    }

    public void sendFoodTruckRegistrationEmail(FoodTruck foodTruck){
        Client client = Client.create();
        client.addFilter(new HTTPBasicAuthFilter("api", API_KEY));
        client.addFilter(new LoggingFilter(System.out));
        WebResource webResource = client
                .resource("https://api.mailgun.net/v2/voodie.mailgun.org/messages");
        Form form = new Form();
        form.add("to", foodTruck.getUser().getEmailAddress());
        form.add("from", "test@voodie.co");
        form.add("subject", "Welcome To Voodie!");
        form.add("text", "Hey " + foodTruck.getUser().getFirstName() + ".  Thanks for registering!");
        ClientResponse response = webResource.accept(MediaType.APPLICATION_FORM_URLENCODED_TYPE)
                .post(ClientResponse.class, form);
        System.out.println(response);
    }
}
