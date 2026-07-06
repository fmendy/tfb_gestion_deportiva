package com.gestion.deportiva.service.impl;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.gestion.deportiva.service.MailService;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import jakarta.activation.DataHandler;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeBodyPart;
import jakarta.mail.internet.MimeMessage;
import jakarta.mail.internet.MimeMultipart;
import jakarta.mail.util.ByteArrayDataSource;

@Service
public class MailServiceImpl implements MailService {

	@Autowired
	private JavaMailSender mailSender;

	@Value("${spring.mail.username}")
	private String mailUsername;

	@Override
	public void enviarMail(List<String> destinatarios, String asunto, String cuerpo)
			throws MessagingException, IOException {
		enviarMail(destinatarios, asunto, cuerpo, null);
	}

	@Override
	public void enviarMail(List<String> destinatarios, String asunto, String cuerpo, String icsContent)
			throws MessagingException, IOException {

		MimeMessage mensaje = mailSender.createMimeMessage();
		MimeMessageHelper helper = new MimeMessageHelper(mensaje, true, "UTF-8");

		helper.setTo("apaneda@transportes.gob.es");
		helper.setSubject(asunto);
		helper.setFrom(mailUsername);
		helper.setText(cuerpo, true);

		if (StringUtils.hasText(icsContent)) {

			// ✅ Determinar correctamente el método del ICS
			// Nunca debe ser "CANCELLED" — debe ser "CANCEL" o "REQUEST"
			String method = icsContent.contains("METHOD:CANCEL") ? "CANCEL" : "REQUEST";

			// 🗓️ Parte 1: cuerpo del correo (HTML)
			MimeBodyPart htmlPart = new MimeBodyPart();
			htmlPart.setContent(cuerpo, "text/html; charset=UTF-8");

			// 🗓️ Parte 2: la invitación/cancelación iCalendar
			MimeBodyPart calendarPart = new MimeBodyPart();
			calendarPart.setDataHandler(new DataHandler(
					new ByteArrayDataSource(icsContent, "text/calendar;method=" + method + ";charset=UTF-8")));

			// ✅ Cabeceras que ayudan a Outlook/Gmail a interpretar la invitación
			calendarPart.setHeader("Content-Class", "urn:content-classes:calendarmessage");
			calendarPart.setHeader("Content-ID", "calendar_message");
			calendarPart.setHeader("Content-Type", "text/calendar; method=" + method + "; charset=UTF-8");
			calendarPart.setHeader("Content-Disposition", "inline");

			// 🔹 Estructura multiparte del mensaje
			MimeMultipart multipart = new MimeMultipart("alternative");
			multipart.addBodyPart(htmlPart); // Parte HTML visible
			multipart.addBodyPart(calendarPart); // Parte ICS que Outlook interpreta

			mensaje.setContent(multipart);
		}

		mailSender.send(mensaje);
	}

}
