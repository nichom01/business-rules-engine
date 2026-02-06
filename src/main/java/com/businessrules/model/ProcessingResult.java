package com.businessrules.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ProcessingResult {
    private boolean success;
    private String message;
    private List<ValidationError> errors = new ArrayList<>();
    private List<Map<String, Object>> transformedData = new ArrayList<>();

    public ProcessingResult() {
    }

    public ProcessingResult(boolean success, String message, List<ValidationError> errors, List<Map<String, Object>> transformedData) {
        this.success = success;
        this.message = message;
        this.errors = errors != null ? errors : new ArrayList<>();
        this.transformedData = transformedData != null ? transformedData : new ArrayList<>();
    }

    public static ProcessingResultBuilder builder() {
        return new ProcessingResultBuilder();
    }

    public void addError(String field, String rule, String message) {
        errors.add(ValidationError.builder()
                .field(field)
                .rule(rule)
                .message(message)
                .build());
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<ValidationError> getErrors() {
        return errors;
    }

    public void setErrors(List<ValidationError> errors) {
        this.errors = errors;
    }

    public List<Map<String, Object>> getTransformedData() {
        return transformedData;
    }

    public void setTransformedData(List<Map<String, Object>> transformedData) {
        this.transformedData = transformedData;
    }

    public static class ProcessingResultBuilder {
        private boolean success;
        private String message;
        private List<ValidationError> errors = new ArrayList<>();
        private List<Map<String, Object>> transformedData = new ArrayList<>();

        public ProcessingResultBuilder success(boolean success) {
            this.success = success;
            return this;
        }

        public ProcessingResultBuilder message(String message) {
            this.message = message;
            return this;
        }

        public ProcessingResultBuilder errors(List<ValidationError> errors) {
            this.errors = errors;
            return this;
        }

        public ProcessingResultBuilder transformedData(List<Map<String, Object>> transformedData) {
            this.transformedData = transformedData;
            return this;
        }

        public ProcessingResult build() {
            return new ProcessingResult(success, message, errors, transformedData);
        }
    }
}
