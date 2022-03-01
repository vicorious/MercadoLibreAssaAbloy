package com.example.springboot.dto.response.orders;

import java.util.List;

public class Buyer
{
    private BillingInfo billing_info;

    private Phone phone;

    private AlternativePhone alternative_phone;

    private String nickname;
    private String last_name;
    private String id;
    private String first_name;
    private String email;

    private int total_amount;
    private int paid_amount;
    private List<Object> mediations;
    private String currency_id;
    private String status;

    public Buyer(BillingInfo billing_info, Phone phone, AlternativePhone alternative_phone, String nickname, String last_name, String id, String first_name, String email, int total_amount, int paid_amount, List<Object> mediations, String currency_id, String status) {
        this.billing_info = billing_info;
        this.phone = phone;
        this.alternative_phone = alternative_phone;
        this.nickname = nickname;
        this.last_name = last_name;
        this.id = id;
        this.first_name = first_name;
        this.email = email;
        this.total_amount = total_amount;
        this.paid_amount = paid_amount;
        this.mediations = mediations;
        this.currency_id = currency_id;
        this.status = status;
    }

    public int getTotal_amount() {
        return total_amount;
    }

    public void setTotal_amount(int total_amount) {
        this.total_amount = total_amount;
    }

    public int getPaid_amount() {
        return paid_amount;
    }

    public void setPaid_amount(int paid_amount) {
        this.paid_amount = paid_amount;
    }

    public List<Object> getMediations() {
        return mediations;
    }

    public void setMediations(List<Object> mediations) {
        this.mediations = mediations;
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

    public BillingInfo getBilling_info() {
        return billing_info;
    }

    public void setBilling_info(BillingInfo billing_info) {
        this.billing_info = billing_info;
    }

    public Phone getPhone() {
        return phone;
    }

    public void setPhone(Phone phone) {
        this.phone = phone;
    }

    public AlternativePhone getAlternative_phone() {
        return alternative_phone;
    }

    public void setAlternative_phone(AlternativePhone alternative_phone) {
        this.alternative_phone = alternative_phone;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getLast_name() {
        return last_name;
    }

    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFirst_name() {
        return first_name;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
