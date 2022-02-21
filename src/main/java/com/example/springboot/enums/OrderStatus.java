package com.example.springboot.enums;

public enum OrderStatus {

    PAID("paid"),
    CONFIRMED("confirmed"),
    PAYMENT_REQUIRED("payment_required"),
    PAYMENT_IN_PROCESS("payment_in_process"),
    PARTIALLY_PAID("partially_paid"),
    CANCELLED("cancelled");
    private String value;

    OrderStatus(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}