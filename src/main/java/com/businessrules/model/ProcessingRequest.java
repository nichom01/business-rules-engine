package com.businessrules.model;

import com.fasterxml.jackson.annotation.JsonAnySetter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ProcessingRequest {
    private List<Map<String, Object>> objects = new ArrayList<>();
    private Map<String, Object> metadata = new HashMap<>();

    @JsonAnySetter
    public void addObject(String key, Object value) {
        if (value instanceof Map) {
            objects.add((Map<String, Object>) value);
        }
    }

    public List<Map<String, Object>> getObjects() {
        return objects;
    }

    public void setObjects(List<Map<String, Object>> objects) {
        this.objects = objects;
    }

    public Map<String, Object> getMetadata() {
        return metadata;
    }

    public void setMetadata(Map<String, Object> metadata) {
        this.metadata = metadata;
    }
}
