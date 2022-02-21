package com.example.springboot.dto.response.orders;

import java.util.List;

public class Item {
    private String seller_custom_field;
    private String condition;
    private String global_price;
    private String category_id;
    private String variation_id;

    private List<Variation> variation_attributes;

    private String seller_sku;
    private String warranty;
    private String id;
    private String title;

    public Item(String seller_custom_field, String condition, String global_price, String category_id, String variation_id, List<Variation> variation_attributes, String seller_sku, String warranty, String id, String title) {
        this.seller_custom_field = seller_custom_field;
        this.condition = condition;
        this.global_price = global_price;
        this.category_id = category_id;
        this.variation_id = variation_id;
        this.variation_attributes = variation_attributes;
        this.seller_sku = seller_sku;
        this.warranty = warranty;
        this.id = id;
        this.title = title;
    }

    public String getSeller_custom_field() {
        return seller_custom_field;
    }

    public void setSeller_custom_field(String seller_custom_field) {
        this.seller_custom_field = seller_custom_field;
    }

    public String getCondition() {
        return condition;
    }

    public void setCondition(String condition) {
        this.condition = condition;
    }

    public String getGlobal_price() {
        return global_price;
    }

    public void setGlobal_price(String global_price) {
        this.global_price = global_price;
    }

    public String getCategory_id() {
        return category_id;
    }

    public void setCategory_id(String category_id) {
        this.category_id = category_id;
    }

    public String getVariation_id() {
        return variation_id;
    }

    public void setVariation_id(String variation_id) {
        this.variation_id = variation_id;
    }

    public List<Variation> getVariation_attributes() {
        return variation_attributes;
    }

    public void setVariation_attributes(List<Variation> variation_attributes) {
        this.variation_attributes = variation_attributes;
    }

    public String getSeller_sku() {
        return seller_sku;
    }

    public void setSeller_sku(String seller_sku) {
        this.seller_sku = seller_sku;
    }

    public String getWarranty() {
        return warranty;
    }

    public void setWarranty(String warranty) {
        this.warranty = warranty;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
