package com.redhat.spring.config;

import com.redhat.spring.service.DemoService;
import com.redhat.spring.service.DevDemoService;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class StandardServiceConfiguration {
    @Bean
    @ConditionalOnProperty(prefix = "spring.profiles", name = "active", havingValue = "", matchIfMissing = true)
    @ConditionalOnMissingBean(DemoService.class)
    public DemoService defaultDemoService() {
        return new DevDemoService();
    }

    @Bean
    @ConditionalOnMissingBean(DemoService.class)
    public DemoService defaultFallbackDemoService() {
        return new DevDemoService();
    }
}
