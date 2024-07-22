package com.redhat.quarkus.service;

import io.quarkus.arc.lookup.LookupIfProperty;
import io.quarkus.arc.profile.IfBuildProfile;
import jakarta.enterprise.context.ApplicationScoped;

//@IfBuildProfile("dev")
@LookupIfProperty(name = "app.env",stringValue = "dev",lookupIfMissing = true)
@ApplicationScoped
public class DevDemoService implements DemoService {
    @Override
    public String demoMethod() {
        return "Executing demo method for DEV demo service [" + this.getClass().getName() + "]";
    }
}
