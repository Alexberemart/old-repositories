package com.alexberemart.basketapi.services;

import java.io.IOException;

public interface EmailService {
    void sendSimpleMessage(
            String to, String subject, String text) throws IOException;

    void sendHTMLMessage(
            String to, String subject, String text);
}
