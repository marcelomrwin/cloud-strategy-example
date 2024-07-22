package com.redhat.spring.service;

public class DevDemoService implements DemoService {
    @Override
    public String demoMethod() {
        return "Executing demo method for DEV demo service [" + this.getClass().getName() + "]";
    }
}
