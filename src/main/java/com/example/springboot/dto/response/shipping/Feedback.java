package com.example.springboot.dto.response.shipping;

public class Feedback {
    private String sale;
    private String purchase;

    public Feedback(String sale, String purchase) {
        this.sale = sale;
        this.purchase = purchase;
    }

    public String getSale() {
        return sale;
    }

    public void setSale(String sale) {
        this.sale = sale;
    }

    public String getPurchase() {
        return purchase;
    }

    public void setPurchase(String purchase) {
        this.purchase = purchase;
    }
}
