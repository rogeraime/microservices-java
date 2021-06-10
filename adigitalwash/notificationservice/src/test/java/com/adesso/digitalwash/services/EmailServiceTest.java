package com.adesso.digitalwash.services;

import static org.assertj.core.api.Assertions.*;

import java.io.IOException;

import javax.mail.BodyPart;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import org.assertj.core.api.SoftAssertions;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;

import com.adesso.digitalwash.api.EmailController;
import com.adesso.digitalwash.model.Mail;

public class EmailServiceTest {
	private SimpleMailMessage templateMock;
	private JavaMailSender emailSenderMock;
	private EmailService testSubject;
	
	@Before
	public void init() {
		templateMock = Mockito.mock(SimpleMailMessage.class);
		emailSenderMock = Mockito.mock(JavaMailSender.class);
		
        testSubject = new EmailService(templateMock, emailSenderMock, "digiwash@adesso.de");
	}
	
	@Test
	public void sendMailWithMailObjectComparedToMessage() throws MailException, MessagingException, IOException {
		//Given
		Mail mail = new Mail();
		mail.setTo("melvin.weiershaeuser@adesso.de");
		mail.setSubject("DigiWash Test");
		mail.setText("Hello, DigiWash is great, keep your laundry clean and tidy!");
		byte[] byteArray = new byte[2];
		byteArray[0] = (byte)00000000;
		byteArray[1] = (byte)11111111;
		mail.setAttachment(byteArray);
		
		JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
		mailSender.setHost("adessomail");
		mailSender.setUsername("digiwash@adesso.de");
		mailSender.setPassword("Hallo123");
		
		MimeMessage messageMock = mailSender.createMimeMessage();
		Mockito.when(emailSenderMock.createMimeMessage()).thenReturn(messageMock);
		
		Mockito.when(templateMock.getText()).thenReturn("\n%s\n");
		
		SoftAssertions softly = new SoftAssertions();
		
		//When
		testSubject.sendMail(mail);
		
		//Then
		softly.assertThat(messageMock.getFrom()[0].toString()).isEqualTo("digiwash@adesso.de");
		softly.assertThat(messageMock.getAllRecipients()[0].toString()).isEqualTo(mail.getTo());
		softly.assertThat(messageMock.getSubject()).isEqualTo(mail.getSubject());
		//softly.assertThat(messageMock.getContent()).isEqualTo(String.format(templateMock.getText(), mail.getText()));
		//softly.assertThat(messageMock.getAttachement).isEqualTo(mail.getAttachment());
		softly.assertAll();
	}
}