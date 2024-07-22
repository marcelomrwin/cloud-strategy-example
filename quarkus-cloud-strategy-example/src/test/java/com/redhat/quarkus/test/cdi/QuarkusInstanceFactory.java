package com.redhat.quarkus.test.cdi;

import com.redhat.quarkus.service.DemoService;
import jakarta.enterprise.inject.Instance;
import jakarta.enterprise.inject.literal.NamedLiteral;
import org.eclipse.microprofile.config.inject.ConfigProperty;

public class QuarkusInstanceFactory {

    final DemoService demoService;

    QuarkusInstanceFactory(@ConfigProperty(name="demo.service.implementation-name") String instanceName,
               Instance<DemoService> demoServiceInstance) {
        demoService = demoServiceInstance.select(NamedLiteral.of(instanceName)).get();
    }

}
