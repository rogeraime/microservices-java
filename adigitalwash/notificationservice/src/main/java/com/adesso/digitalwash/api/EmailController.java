package com.adesso.digitalwash.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.MailException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.adesso.digitalwash.model.Mail;
import com.adesso.digitalwash.services.EmailService;
import javax.mail.MessagingException;


@Controller
@RequestMapping("/send")
public class EmailController {

	public EmailService emailService;
	
	@Autowired
	public EmailController(EmailService emailService) {
		this.emailService = emailService;
	}

	@PostMapping("/mail")
	public ResponseEntity<String> sendMail(@RequestBody Mail mail) throws MailException, MessagingException {
		try{
			emailService.sendMail(mail);
			return new ResponseEntity<String>("E-Mail sent successfully", null, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<String>("E-Mail sent Error", null, HttpStatus.CONFLICT);
		}
		

	}
}