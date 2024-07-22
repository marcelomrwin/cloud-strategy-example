package com.redhat.quarkus.test;

import com.redhat.quarkus.service.DemoService;
import com.redhat.quarkus.service.UatDemoService;
import com.redhat.quarkus.test.profiles.UatTestProfile;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.junit.TestProfile;
import jakarta.enterprise.inject.Instance;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

@QuarkusTest
@TestProfile(UatTestProfile.class)
public class DemoServiceUATTest {
    @Inject
    Instance<DemoService> demoServiceInstance;

    @Test
    public void testUATEnvironmentDemoService() {
        DemoService demoService = demoServiceInstance.get();
        assertThat(demoService).isNotNull();
        String response = demoService.demoMethod();
        assertThat(response).isNotEmpty().isNotNull();
        assertThat(response).contains(UatDemoService.class.getName());

    }
}
