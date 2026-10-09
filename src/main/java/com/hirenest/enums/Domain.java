package com.hirenest.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

import java.util.Arrays;
import java.util.stream.Collectors;


public enum Domain {
    JAVA("Java"),
    PYTHON("Python"),
    REACT_JS("React Js"),
    MERN_STACK("Mern Stack"),
    DEVOPS("DevOps"),
    DEVSECOPS("DevSecOps"),
    DATA_ANALYST("Data Analyst"),
    DATA_ENGINEERING("Data Engineering"),
    DATA_SCIENCE("Data Science");

    private final String displayName;

    Domain(String displayName) {
        this.displayName = displayName;
    }

    @JsonValue
    public String getDisplayName() {
        return displayName;
    }

    @JsonCreator
    public static Domain fromValue(String value) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("domain is required. Allowed values: " + allowedValues());
        }
        String key = normalize(value);
        for (Domain d : values()) {
            if (normalize(d.name()).equals(key) || normalize(d.displayName).equals(key)) {
                return d;
            }
        }
        throw new IllegalArgumentException("Invalid domain '" + value + "'. Allowed values: " + allowedValues());
    }

    public static String allowedValues() {
        return Arrays.stream(values()).map(Domain::getDisplayName).collect(Collectors.joining(", "));
    }

    
    private static String normalize(String s) {
        return s.trim().toLowerCase().replaceAll("[\\s_\\-]", "");
    }
}
