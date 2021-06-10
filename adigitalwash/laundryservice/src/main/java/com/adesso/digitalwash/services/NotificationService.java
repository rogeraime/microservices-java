package com.adesso.digitalwash.services;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.web.client.AsyncRestTemplate;

import com.adesso.digitalwash.model.Mail;


@Service
public class NotificationService {
      private String NotificationServer = "http://localhost:8080";
      private AsyncRestTemplate rest;

      public NotificationService() {
            this.rest = new AsyncRestTemplate();
          }

    public void sendMail(Mail mail, HttpHeaders headers) {
        String uri = "/send/mail";
        HttpEntity<Mail> requestEntity = new HttpEntity<Mail>(mail, headers);
        try {
        	rest.postForEntity(NotificationServer + uri, requestEntity, String.class);
        } catch (Exception e) {
        	e.printStackTrace();
        }
    }
}