package com.redhat.quarkus.test.profiles;

import io.quarkus.test.junit.QuarkusTestProfile;

import java.util.Map;

public class UatTestProfile implements QuarkusTestProfile {

    @Override
    public Map<String, String> getConfigOverrides() {
        return Map.of("app.env", "uat");
    }
}
