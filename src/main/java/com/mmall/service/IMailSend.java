package com.mmall.service;

import org.springframework.mail.SimpleMailMessage;

public interface IMailSend {
    public void send(SimpleMailMessage mail);
}
