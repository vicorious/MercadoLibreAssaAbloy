package com.example.springboot.dto.response.orders;

public class ATMTransfer {
    private String transaction_id;
    private String company_id;

    public ATMTransfer(String transaction_id, String company_id) {
        this.transaction_id = transaction_id;
        this.company_id = company_id;
    }

    public String getTransaction_id() {
        return transaction_id;
    }

    public void setTransaction_id(String transaction_id) {
        this.transaction_id = transaction_id;
    }

    public String getCompany_id() {
        return company_id;
    }

    public void setCompany_id(String company_id) {
        this.company_id = company_id;
    }
}
