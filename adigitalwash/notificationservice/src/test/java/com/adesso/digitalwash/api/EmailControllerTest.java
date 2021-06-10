package com.adesso.digitalwash.api;

import static org.assertj.core.api.Assertions.*;

import javax.mail.MessagingException;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.MailException;

import com.adesso.digitalwash.model.Mail;
import com.adesso.digitalwash.services.EmailService;

public class EmailControllerTest {
	private EmailService emailServiceMock;
	private EmailController testSubject;
	
	@Before
	public void init() {
		emailServiceMock = Mockito.mock(EmailService.class);
        testSubject = new EmailController(emailServiceMock);
	}
	
	@Test
	public void sendMailreturnsSuccessMessage() throws MailException, MessagingException {
		//Given
		Mail mail = new Mail();
		ResponseEntity<String> success = new ResponseEntity<String>("E-Mail sent successfully", null, HttpStatus.OK);
		
		//When
		assertThat(testSubject.sendMail(mail))
		
		//Then
		.isEqualTo(success);	
	}
	
	@Test
	public void sendMailreturnsErrorMessage() throws MailException, MessagingException {
		//Given
		Mail mail = new Mail();
		ResponseEntity<String> error = new ResponseEntity<String>("E-Mail sent Error", null, HttpStatus.CONFLICT);
		Mockito.doThrow(new MessagingException()).when(emailServiceMock).sendMail(mail);
		
		//When
		assertThat(testSubject.sendMail(mail))
		
		//Then
		.isEqualTo(error);	
	}
}
