package com.example.springboot.dto.response.orders;

import java.util.List;

public class Payment {
    private String reason;
    private String status_code;
    private int total_paid_amount;
    private String operation_type;
    private int transaction_amount;
    private String date_approved;
    private Collector collector;

    private String coupon_id;
    private int installments;
    private String authorization_code;
    private int taxes_amount;
    private String id;
    private String date_last_modified;
    private int coupon_amount;
    private List<String> available_actions;

    private int shipping_cost;
    private int installment_amount;
    private String date_created;
    private String activation_uri;
    private int overpaid_amount;
    private String card_id;
    private String status_detail;
    private String issuer_id;
    private String payment_method_id;
    private String payment_type;
    private String deferred_period;

    private ATMTransfer atm_transfer_reference;

    private String site_id;
    private String payer_id;
    private String order_id;
    private String currency_id;
    private String status;
    private String transaction_order_id;

    public Payment(String reason, String status_code, int total_paid_amount, String operation_type, int transaction_amount, String date_approved, Collector collector, String coupon_id, int installments, String authorization_code, int taxes_amount, String id, String date_last_modified, int coupon_amount, List<String> available_actions, int shipping_cost, int installment_amount, String date_created, String activation_uri, int overpaid_amount, String card_id, String status_detail, String issuer_id, String payment_method_id, String payment_type, String deferred_period, ATMTransfer atm_transfer_reference, String site_id, String payer_id, String order_id, String currency_id, String status, String transaction_order_id) {
        this.reason = reason;
        this.status_code = status_code;
        this.total_paid_amount = total_paid_amount;
        this.operation_type = operation_type;
        this.transaction_amount = transaction_amount;
        this.date_approved = date_approved;
        this.collector = collector;
        this.coupon_id = coupon_id;
        this.installments = installments;
        this.authorization_code = authorization_code;
        this.taxes_amount = taxes_amount;
        this.id = id;
        this.date_last_modified = date_last_modified;
        this.coupon_amount = coupon_amount;
        this.available_actions = available_actions;
        this.shipping_cost = shipping_cost;
        this.installment_amount = installment_amount;
        this.date_created = date_created;
        this.activation_uri = activation_uri;
        this.overpaid_amount = overpaid_amount;
        this.card_id = card_id;
        this.status_detail = status_detail;
        this.issuer_id = issuer_id;
        this.payment_method_id = payment_method_id;
        this.payment_type = payment_type;
        this.deferred_period = deferred_period;
        this.atm_transfer_reference = atm_transfer_reference;
        this.site_id = site_id;
        this.payer_id = payer_id;
        this.order_id = order_id;
        this.currency_id = currency_id;
        this.status = status;
        this.transaction_order_id = transaction_order_id;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getStatus_code() {
        return status_code;
    }

    public void setStatus_code(String status_code) {
        this.status_code = status_code;
    }

    public int getTotal_paid_amount() {
        return total_paid_amount;
    }

    public void setTotal_paid_amount(int total_paid_amount) {
        this.total_paid_amount = total_paid_amount;
    }

    public String getOperation_type() {
        return operation_type;
    }

    public void setOperation_type(String operation_type) {
        this.operation_type = operation_type;
    }

    public int getTransaction_amount() {
        return transaction_amount;
    }

    public void setTransaction_amount(int transaction_amount) {
        this.transaction_amount = transaction_amount;
    }

    public String getDate_approved() {
        return date_approved;
    }

    public void setDate_approved(String date_approved) {
        this.date_approved = date_approved;
    }

    public Collector getCollector() {
        return collector;
    }

    public void setCollector(Collector collector) {
        this.collector = collector;
    }

    public String getCoupon_id() {
        return coupon_id;
    }

    public void setCoupon_id(String coupon_id) {
        this.coupon_id = coupon_id;
    }

    public int getInstallments() {
        return installments;
    }

    public void setInstallments(int installments) {
        this.installments = installments;
    }

    public String getAuthorization_code() {
        return authorization_code;
    }

    public void setAuthorization_code(String authorization_code) {
        this.authorization_code = authorization_code;
    }

    public int getTaxes_amount() {
        return taxes_amount;
    }

    public void setTaxes_amount(int taxes_amount) {
        this.taxes_amount = taxes_amount;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDate_last_modified() {
        return date_last_modified;
    }

    public void setDate_last_modified(String date_last_modified) {
        this.date_last_modified = date_last_modified;
    }

    public int getCoupon_amount() {
        return coupon_amount;
    }

    public void setCoupon_amount(int coupon_amount) {
        this.coupon_amount = coupon_amount;
    }

    public List<String> getAvailable_actions() {
        return available_actions;
    }

    public void setAvailable_actions(List<String> available_actions) {
        this.available_actions = available_actions;
    }

    public int getShipping_cost() {
        return shipping_cost;
    }

    public void setShipping_cost(int shipping_cost) {
        this.shipping_cost = shipping_cost;
    }

    public int getInstallment_amount() {
        return installment_amount;
    }

    public void setInstallment_amount(int installment_amount) {
        this.installment_amount = installment_amount;
    }

    public String getDate_created() {
        return date_created;
    }

    public void setDate_created(String date_created) {
        this.date_created = date_created;
    }

    public String getActivation_uri() {
        return activation_uri;
    }

    public void setActivation_uri(String activation_uri) {
        this.activation_uri = activation_uri;
    }

    public int getOverpaid_amount() {
        return overpaid_amount;
    }

    public void setOverpaid_amount(int overpaid_amount) {
        this.overpaid_amount = overpaid_amount;
    }

    public String getCard_id() {
        return card_id;
    }

    public void setCard_id(String card_id) {
        this.card_id = card_id;
    }

    public String getStatus_detail() {
        return status_detail;
    }

    public void setStatus_detail(String status_detail) {
        this.status_detail = status_detail;
    }

    public String getIssuer_id() {
        return issuer_id;
    }

    public void setIssuer_id(String issuer_id) {
        this.issuer_id = issuer_id;
    }

    public String getPayment_method_id() {
        return payment_method_id;
    }

    public void setPayment_method_id(String payment_method_id) {
        this.payment_method_id = payment_method_id;
    }

    public String getPayment_type() {
        return payment_type;
    }

    public void setPayment_type(String payment_type) {
        this.payment_type = payment_type;
    }

    public String getDeferred_period() {
        return deferred_period;
    }

    public void setDeferred_period(String deferred_period) {
        this.deferred_period = deferred_period;
    }

    public ATMTransfer getAtm_transfer_reference() {
        return atm_transfer_reference;
    }

    public void setAtm_transfer_reference(ATMTransfer atm_transfer_reference) {
        this.atm_transfer_reference = atm_transfer_reference;
    }

    public String getSite_id() {
        return site_id;
    }

    public void setSite_id(String site_id) {
        this.site_id = site_id;
    }

    public String getPayer_id() {
        return payer_id;
    }

    public void setPayer_id(String payer_id) {
        this.payer_id = payer_id;
    }

    public String getOrder_id() {
        return order_id;
    }

    public void setOrder_id(String order_id) {
        this.order_id = order_id;
    }

    public String getCurrency_id() {
        return currency_id;
    }

    public void setCurrency_id(String currency_id) {
        this.currency_id = currency_id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getTransaction_order_id() {
        return transaction_order_id;
    }

    public void setTransaction_order_id(String transaction_order_id) {
        this.transaction_order_id = transaction_order_id;
    }
}
