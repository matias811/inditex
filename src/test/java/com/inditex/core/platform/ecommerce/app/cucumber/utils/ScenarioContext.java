package com.inditex.core.platform.ecommerce.app.cucumber.utils;

import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class ScenarioContext {
    private final Map<String, Object> scenarioData = new HashMap<>();

    public void set(String key, Object value) {
        scenarioData.put(key, value);
    }

    public <T> T get(String key, Class<T> type) {
        return type.cast(scenarioData.get(key));
    }

    public boolean contains(String key) {
        return scenarioData.containsKey(key);
    }
}
