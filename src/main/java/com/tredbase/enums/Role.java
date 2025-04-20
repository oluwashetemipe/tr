package com.tredbase.enums;

public enum Role {
    ADMIN("Admin user"),CUSTOMER("Customer user");

    private final String label;

    Role(String label) {
        this.label = label;
    }


    public String getLabel() {
        return label;
    }

}
