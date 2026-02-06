package com.businessrules.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RuleContext {
    private Map<String, Object> data;
    private List<ValidationError> errors = new ArrayList<>();
    private Map<String, Object> transformedData = new HashMap<>();
    private String ruleGroup;

    public RuleContext(Map<String, Object> data, String ruleGroup) {
        this.data = data;
        this.ruleGroup = ruleGroup;
    }

    public void addError(String field, String rule, String message) {
        errors.add(ValidationError.builder()
                .field(field)
                .rule(rule)
                .message(message)
                .build());
    }

    public Map<String, Object> getData() {
        return data;
    }

    public void setData(Map<String, Object> data) {
        this.data = data;
    }

    public List<ValidationError> getErrors() {
        return errors;
    }

    public void setErrors(List<ValidationError> errors) {
        this.errors = errors;
    }

    public Map<String, Object> getTransformedData() {
        return transformedData;
    }

    public void setTransformedData(Map<String, Object> transformedData) {
        this.transformedData = transformedData;
    }

    public String getRuleGroup() {
        return ruleGroup;
    }

    public void setRuleGroup(String ruleGroup) {
        this.ruleGroup = ruleGroup;
    }
}
