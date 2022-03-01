package com.example.springboot.dto.response.orders;

import com.example.springboot.dto.response.shipping.ShippingParent;

import java.util.List;

public class OrderResponse {

    private Buyer buyer;

    private List<OrderItem> order_items;
    
    private List<Payment> payments;

    private ShippingParent shipping_id;


    public OrderResponse(Buyer buyer, List<OrderItem> order_items, List<Payment> payments, ShippingParent shipping_id) {
        this.buyer = buyer;
        this.order_items = order_items;
        this.payments = payments;
        this.shipping_id = shipping_id;
    }

    public List<Payment> getPayments() {
        return payments;
    }

    public void setPayments(List<Payment> payments) {
        this.payments = payments;
    }

    public ShippingParent getShipping_id() {
        return shipping_id;
    }

    public void setShipping_id(ShippingParent shipping_id) {
        this.shipping_id = shipping_id;
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
