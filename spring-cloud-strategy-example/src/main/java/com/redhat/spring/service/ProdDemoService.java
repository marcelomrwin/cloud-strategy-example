package com.redhat.spring.service;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

@Service
@ConditionalOnProperty(prefix = "spring.profiles", name = "active",havingValue = "prod")
public class ProdDemoService implements DemoService {
    @Override
    public String demoMethod() {
        return "Executing demo method for PROD demo service [" + this.getClass().getName() + "]";
    }
}
