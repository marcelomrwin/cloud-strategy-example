package com.redhat.spring.test;

import com.redhat.spring.DemoServiceApplication;
import com.redhat.spring.service.DemoService;
import com.redhat.spring.service.UatDemoService;
import com.redhat.spring.service.DevDemoService;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.runner.ApplicationContextRunner;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.notNullValue;

@SpringBootTest
public class DemoServiceUATTest {

    private final ApplicationContextRunner contextRunner = new ApplicationContextRunner()
            .withUserConfiguration(DemoServiceApplication.class);

    @Test
    public void testEnvironmentDemoService() {
        contextRunner
                .withPropertyValues("spring.profiles.active=uat")
                .run(context -> {
                    DemoService demoService = context.getBean(DemoService.class);
                    assertThat(demoService, notNullValue());
                    String response = demoService.demoMethod();
                    assertThat(response, notNullValue());
                    assertThat(response, containsString(UatDemoService.class.getName()));
                    org.assertj.core.api.Assertions.assertThat(context).hasSingleBean(UatDemoService.class);
                    org.assertj.core.api.Assertions.assertThat(context).doesNotHaveBean(DevDemoService.class);
                });
    }
}
