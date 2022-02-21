package com.example.springboot.dto.response.shipping;

public class Shipping {

    private String id;

    public Shipping(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
