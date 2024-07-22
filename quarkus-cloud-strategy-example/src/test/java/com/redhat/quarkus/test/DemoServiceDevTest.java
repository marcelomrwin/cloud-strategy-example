package com.redhat.quarkus.test;

import com.redhat.quarkus.service.DemoService;
import com.redhat.quarkus.service.DevDemoService;
import com.redhat.quarkus.test.profiles.DevTestProfile;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.junit.TestProfile;
import jakarta.enterprise.inject.Instance;
import jakarta.inject.Inject;
import jakarta.ws.rs.SeBootstrap;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

@QuarkusTest
@TestProfile(DevTestProfile.class)
public class DemoServiceDevTest {

    @Inject
    Instance<DemoService> demoServiceInstance;

    @Test
    public void testStandardDemoService() {
        DemoService demoService = demoServiceInstance.get();
        String response = demoService.demoMethod();
        assertThat(response).isNotNull().isNotEmpty();
        assertThat(response).contains(DevDemoService.class.getName());
    }
}
