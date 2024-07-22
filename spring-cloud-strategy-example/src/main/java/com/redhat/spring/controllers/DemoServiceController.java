package com.redhat.spring.controllers;

import com.redhat.spring.service.DemoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/api",produces = MediaType.TEXT_PLAIN_VALUE)
public class DemoServiceController {
    @Autowired
    private DemoService demoService;

    @GetMapping
    public String sayHello() {
        return demoService.demoMethod();
    }
}
