package com.example.springboot.dto.response.shipping;

public class ShippingParent {
    private String expiration_date;
    private Feedback feedback;
    private Shipping shipping;

    private String date_closed;
    private String id;
    private String manufacturing_ending_date;

    public ShippingParent(String expiration_date, Feedback feedback, Shipping shipping, String date_closed, String id, String manufacturing_ending_date) {
        this.expiration_date = expiration_date;
        this.feedback = feedback;
        this.shipping = shipping;
        this.date_closed = date_closed;
        this.id = id;
        this.manufacturing_ending_date = manufacturing_ending_date;
    }

    public String getExpiration_date() {
        return expiration_date;
    }

    public void setExpiration_date(String expiration_date) {
        this.expiration_date = expiration_date;
    }

    public Feedback getFeedback() {
        return feedback;
    }

    public void setFeedback(Feedback feedback) {
        this.feedback = feedback;
    }

    public Shipping getShipping() {
        return shipping;
    }

    public void setShipping(Shipping shipping) {
        this.shipping = shipping;
    }

    public String getDate_closed() {
        return date_closed;
    }

    public void setDate_closed(String date_closed) {
        this.date_closed = date_closed;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getManufacturing_ending_date() {
        return manufacturing_ending_date;
    }

    public void setManufacturing_ending_date(String manufacturing_ending_date) {
        this.manufacturing_ending_date = manufacturing_ending_date;
    }
}
