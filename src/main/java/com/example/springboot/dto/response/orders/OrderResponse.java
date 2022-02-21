package com.example.springboot.dto.response.orders;

import java.util.List;

public class OrderResponse {

    private Buyer buyer;

    private List<OrderItem> order_items;

    private int total_amount;
    private int paid_amount;
    private List<Object> mediations;
    private String currency_id;
    private String status;

    public OrderResponse(Buyer buyer, List<OrderItem> order_items) {
        this.buyer = buyer;
        this.order_items = order_items;
    }

    public Buyer getBuyer() {
        return buyer;
    }

    public void setBuyer(Buyer buyer) {
        this.buyer = buyer;
    }

    public List<OrderItem> getOrder_items() {
        return order_items;
    }

    public void setOrder_items(List<OrderItem> order_items) {
        this.order_items = order_items;
    }
}
