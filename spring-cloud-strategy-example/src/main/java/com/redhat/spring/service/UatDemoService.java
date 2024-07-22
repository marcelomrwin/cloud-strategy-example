package com.redhat.spring.service;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

@Service
@ConditionalOnProperty(prefix = "spring.profiles", name = "active",havingValue = "uat")
public class UatDemoService implements DemoService {
    @Override
    public String demoMethod() {
        return "Executing demo method for UAT demo service [" + this.getClass().getName() + "]";
    }
}
