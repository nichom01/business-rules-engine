package com.businessrules.model;

public class ValidationError {
    private String field;
    private String rule;
    private String message;

    public ValidationError() {
    }

    public ValidationError(String field, String rule, String message) {
        this.field = field;
        this.rule = rule;
        this.message = message;
    }

    public static ValidationErrorBuilder builder() {
        return new ValidationErrorBuilder();
    }

    public String getField() {
        return field;
    }

    public void setField(String field) {
        this.field = field;
    }

    public String getRule() {
        return rule;
    }

    public void setRule(String rule) {
        this.rule = rule;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public static class ValidationErrorBuilder {
        private String field;
        private String rule;
        private String message;

        public ValidationErrorBuilder field(String field) {
            this.field = field;
            return this;
        }

        public ValidationErrorBuilder rule(String rule) {
            this.rule = rule;
            return this;
        }

        public ValidationErrorBuilder message(String message) {
            this.message = message;
            return this;
        }

        public ValidationError build() {
            return new ValidationError(field, rule, message);
        }
    }
}
