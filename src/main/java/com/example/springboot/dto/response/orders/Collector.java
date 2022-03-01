package com.example.springboot.dto.response.orders;

public class Collector {
    private String id;


    public Collector(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
