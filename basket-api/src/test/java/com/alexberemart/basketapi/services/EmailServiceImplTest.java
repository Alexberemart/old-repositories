package com.alexberemart.basketapi.services;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.mail.javamail.JavaMailSender;

@RunWith(MockitoJUnitRunner.class)
public class EmailServiceImplTest {

    @Mock
    public JavaMailSender emailSender;
    protected EmailServiceImpl emailService;

    @Before
    public void setUp() {
        emailService = new EmailServiceImpl(emailSender);
    }

    @Test
    public void sendSimpleMessage() {
        emailService.sendSimpleMessage("a", "a2", "a");
    }
}