package com.thanhhuong.rookbooks.service;

public interface EmailService {
    void sendSimpleEmail(String to, String subject, String text);
}
