package com.redhat.spring.test;

import com.redhat.spring.DemoServiceApplication;
import com.redhat.spring.service.DemoService;
import com.redhat.spring.service.UatDemoService;
import com.redhat.spring.service.DevDemoService;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.runner.ApplicationContextRunner;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.notNullValue;

@SpringBootTest
public class DemoServiceDevTest {
    Logger logger = LoggerFactory.getLogger(getClass());
    @Resource
    DemoService demoService;

    private static ApplicationContextRunner contextRunner;

    @BeforeAll
    static void setup() {
        contextRunner = new ApplicationContextRunner()
                .withUserConfiguration(DemoServiceApplication.class);
    }

    @Test
    public void testStandardDemoService() {
        String response = demoService.demoMethod();
        assertThat(response, notNullValue());
        assertThat(response, containsString(DevDemoService.class.getName()));
        contextRunner.run((context) -> org.assertj.core.api.Assertions.assertThat(context).doesNotHaveBean(UatDemoService.class));
    }
}
