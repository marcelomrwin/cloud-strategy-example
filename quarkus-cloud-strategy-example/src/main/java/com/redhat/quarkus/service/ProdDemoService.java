package com.redhat.quarkus.service;

import io.quarkus.arc.lookup.LookupIfProperty;
import io.quarkus.arc.profile.IfBuildProfile;
import jakarta.enterprise.context.ApplicationScoped;

//@IfBuildProfile("prod")
@LookupIfProperty(name = "app.env", stringValue = "prod")
@ApplicationScoped
public class ProdDemoService implements DemoService {
    @Override
    public String demoMethod() {
        return "Executing demo method for PROD demo service [" + this.getClass().getName() + "]";
    }
}
