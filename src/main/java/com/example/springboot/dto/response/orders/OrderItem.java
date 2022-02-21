package com.example.springboot.dto.response.orders;

public class OrderItem {

    private Item item;
    private int quantity;
    private int sale_fee;
    private String listing_type_id;
    private int unit_price;
    private int full_unit_price;
    private String currency_id;
    private String manufacturing_days;

    public OrderItem(Item item, int quantity, int sale_fee, String listing_type_id, int unit_price, int full_unit_price, String currency_id, String manufacturing_days) {
        this.item = item;
        this.quantity = quantity;
        this.sale_fee = sale_fee;
        this.listing_type_id = listing_type_id;
        this.unit_price = unit_price;
        this.full_unit_price = full_unit_price;
        this.currency_id = currency_id;
        this.manufacturing_days = manufacturing_days;
    }

    public Item getItem() {
        return item;
    }

    public void setItem(Item item) {
        this.item = item;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public int getSale_fee() {
        return sale_fee;
    }

    public void setSale_fee(int sale_fee) {
        this.sale_fee = sale_fee;
    }

    public String getListing_type_id() {
        return listing_type_id;
    }

    public void setListing_type_id(String listing_type_id) {
        this.listing_type_id = listing_type_id;
    }

    public int getUnit_price() {
        return unit_price;
    }

    public void setUnit_price(int unit_price) {
        this.unit_price = unit_price;
    }

    public int getFull_unit_price() {
        return full_unit_price;
    }

    public void setFull_unit_price(int full_unit_price) {
        this.full_unit_price = full_unit_price;
    }

    public String getCurrency_id() {
        return currency_id;
    }

    public void setCurrency_id(String currency_id) {
        this.currency_id = currency_id;
    }

    public String getManufacturing_days() {
        return manufacturing_days;
    }

    public void setManufacturing_days(String manufacturing_days) {
        this.manufacturing_days = manufacturing_days;
    }
}
