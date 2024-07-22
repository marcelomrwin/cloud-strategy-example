package com.redhat.quarkus.api;

import com.redhat.quarkus.service.DemoService;
import jakarta.enterprise.inject.Instance;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

@Path("/api")
@Produces(MediaType.TEXT_PLAIN)
public class DemoServiceResource {
    @Inject
    Instance<DemoService> demoServiceInstance;

    @GET
    public String sayHello() {
        return demoServiceInstance.get().demoMethod();
    }
}
