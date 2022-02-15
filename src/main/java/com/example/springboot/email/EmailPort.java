package com.example.springboot.email;

public interface EmailPort {
    boolean sendEmail(EmailBody emailBody);
}