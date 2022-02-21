package com.example.springboot.dto.response.orders;

public class BillingInfo
{
    private String doc_number;
    private String doc_type;

    public BillingInfo(String doc_number, String doc_type) {
        this.doc_number = doc_number;
        this.doc_type = doc_type;
    }

    public String getDoc_number() {
        return doc_number;
    }

    public void setDoc_number(String doc_number) {
        this.doc_number = doc_number;
    }

    public String getDoc_type() {
        return doc_type;
    }

    public void setDoc_type(String doc_type) {
        this.doc_type = doc_type;
    }
}
