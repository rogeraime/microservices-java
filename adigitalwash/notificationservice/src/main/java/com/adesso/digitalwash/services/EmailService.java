package com.adesso.digitalwash.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import com.adesso.digitalwash.model.Mail;

import java.util.Objects;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.mail.util.ByteArrayDataSource;

@Service
public class EmailService {

	@Autowired
	public SimpleMailMessage template;

	@Autowired
	public JavaMailSender emailSender;

	@Value("${spring.adessomail.email}")
	private String from;
	
	public EmailService() {
		
	}
	
	public EmailService(SimpleMailMessage template, JavaMailSender emailSender, String from) {
		this.template = template;
		this.emailSender = emailSender;
		this.from = from;
	}
	
	public void sendMail(Mail mail)
			throws MailException, MessagingException {
		Objects.requireNonNull(mail);
		MimeMessage message = emailSender.createMimeMessage();
		// pass 'true' to the constructor to create a multipart message
		MimeMessageHelper helper = new MimeMessageHelper(message, true);

		helper.setFrom(from);
		helper.setTo(mail.getTo());
		helper.setSubject(mail.getSubject());
		String text = String.format(template.getText(), mail.getText());
		helper.setText(text);
		if(mail.getAttachment() != null) {
			helper.addAttachment("Rechnung.pdf", new ByteArrayDataSource(mail.getAttachment(), "application/pdf"));
		}

		emailSender.send(message);
	}
}
