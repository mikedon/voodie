package com.voodie.remote.api;

import com.google.common.collect.Sets;
import org.jboss.resteasy.plugins.interceptors.CorsFilter;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;
import java.util.Set;

@ApplicationPath("/api")
public class RESTApplication extends Application {

    private Set<Object> singletons = Sets.newHashSet();
    private Set<Class<?>> classes = Sets.newHashSet();

    public RESTApplication(){
        // because of spring security We have to use SimpleCORSFilter instead.
//        CorsFilter corsFilter = new CorsFilter();
//        corsFilter.getAllowedOrigins().add("http://localhost:8000");
//        singletons.add(corsFilter);

        classes.add(ElectionREST.class);
        classes.add(FoodieREST.class);
        classes.add(FoodTruckREST.class);
        classes.add(UserREST.class);

    }

    @Override
    public Set<Class<?>> getClasses() {
        return classes;
    }

    @Override
    public Set<Object> getSingletons() {
        return singletons;
    }
}
