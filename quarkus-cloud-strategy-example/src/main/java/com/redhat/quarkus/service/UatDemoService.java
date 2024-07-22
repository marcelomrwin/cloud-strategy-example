package com.redhat.quarkus.service;

import io.quarkus.arc.lookup.LookupIfProperty;
import io.quarkus.arc.profile.IfBuildProfile;
import jakarta.enterprise.context.ApplicationScoped;

//@IfBuildProfile("uat")
@LookupIfProperty(name = "app.env", stringValue = "uat")
@ApplicationScoped
public class UatDemoService implements DemoService {
    @Override
    public String demoMethod() {
        return "Executing demo method for UAT demo service [" + this.getClass().getName() + "]";
    }
}
