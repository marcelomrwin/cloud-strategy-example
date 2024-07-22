package com.redhat.quarkus.test.profiles;

import io.quarkus.test.junit.QuarkusTestProfile;

import java.util.Map;

public class DevTestProfile implements QuarkusTestProfile {

//    @Override
//    public String getConfigProfile() {
//        return "dev";
//    }

    @Override
    public Map<String, String> getConfigOverrides() {
        return Map.of("app.env", "dev");
    }
}
