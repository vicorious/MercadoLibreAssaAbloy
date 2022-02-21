package com.example.springboot.dto.response.orders;

public class Variation {
    private String variation_id;
    private String id;
    private String value_id;
    private String value_name;

    public Variation(String variation_id, String id, String value_id, String value_name) {
        this.variation_id = variation_id;
        this.id = id;
        this.value_id = value_id;
        this.value_name = value_name;
    }

    public String getVariation_id() {
        return variation_id;
    }

    public void setVariation_id(String variation_id) {
        this.variation_id = variation_id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getValue_id() {
        return value_id;
    }

    public void setValue_id(String value_id) {
        this.value_id = value_id;
    }

    public String getValue_name() {
        return value_name;
    }

    public void setValue_name(String value_name) {
        this.value_name = value_name;
    }
}
