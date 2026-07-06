package com.gestion.deportiva.service.impl;

import java.io.IOException;
import java.text.MessageFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.gestion.deportiva.model.Reserva;
import com.gestion.deportiva.service.MailService;
import com.gestion.deportiva.util.Utils;

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

	public void enviarMail(List<String> destinatarios, String asunto, String cuerpo, String icsContent)
			throws MessagingException, IOException {
		MimeMessage mensaje = mailSender.createMimeMessage(); // O tu objeto Message correspondiente
		MimeMessageHelper helper = new MimeMessageHelper(mensaje, true, "UTF-8");

		helper.setTo("apaneda@transportes.gob.es");
		helper.setSubject(asunto);

		if (StringUtils.hasText(icsContent)) {
			String method = icsContent.contains("METHOD:CANCEL") ? "CANCEL" : "REQUEST";

			// Parte 1: cuerpo del correo (HTML)
			MimeBodyPart htmlPart = new MimeBodyPart();
			htmlPart.setContent(cuerpo, "text/html; charset=UTF-8");

			// Parte 2: la invitación iCalendar
			MimeBodyPart calendarPart = new MimeBodyPart();
			calendarPart.setDataHandler(new DataHandler(
					new ByteArrayDataSource(icsContent, "text/calendar;method=" + method + ";charset=UTF-8")));

			// Cabeceras de integración nativa para Outlook/Gmail
			calendarPart.setHeader("Content-Class", "urn:content-classes:calendarmessage");
			calendarPart.setHeader("Content-ID", "calendar_message");
			calendarPart.setHeader("Content-Type", "text/calendar; method=" + method + "; charset=UTF-8");
			calendarPart.setHeader("Content-Disposition", "inline");

			// Mezclar estructura multiparte
			MimeMultipart multipart = new MimeMultipart("alternative");
			multipart.addBodyPart(htmlPart);
			multipart.addBodyPart(calendarPart);

			mensaje.setContent(multipart);
		} else {
			// Fallback si no hay calendario (envío HTML normal)
			helper.setText(cuerpo, true);
		}

		mailSender.send(mensaje);
	}

	@Override
	public void mensajeAprobacionReserva(Reserva reserva) throws MessagingException, IOException {

		// 1. Construcción del asunto y cuerpo HTML del correo
		String asunto = Utils.getMessage("mail.reserva.asunto.aprobada");
		String cuerpo = "<h1>" + Utils.getMessage("mail.reserva.cuerpo.datos") + "</h1></br>";
		cuerpo = cuerpo + "<b>" + Utils.getMessage("mail.reserva.cuerpo.localizacion") + "</b>: "
				+ reserva.getInstalacion().getSede().getMunicipio().getProvincia().getComunidadAutonoma().getNombre()
				+ " - " + reserva.getInstalacion().getSede().getMunicipio().getProvincia().getNombre() + " - "
				+ reserva.getInstalacion().getSede().getMunicipio().getNombre() + " - "
				+ reserva.getInstalacion().getSede().getDireccion() + "<br>";
		cuerpo = cuerpo + "<b>" + Utils.getMessage("mail.reserva.cuerpo.instalacion") + "</b>: "
				+ reserva.getInstalacion().getNombre() + "(" + reserva.getInstalacion().getInstalacionTipo().getNombre()
				+ ")<br>";
		cuerpo = cuerpo + "<b>" + Utils.getMessage("mail.reserva.cuerpo.fecha") + "</b>: " + reserva.getFecha()
				+ "<br>";
		cuerpo = cuerpo + "<b>" + Utils.getMessage("mail.reserva.cuerpo.hora.inicio") + "</b>: "
				+ reserva.getHoraInicio() + "<br>";
		cuerpo = cuerpo + "<b>" + Utils.getMessage("mail.reserva.cuerpo.hora.fin") + "</b>: " + reserva.getHoraFin()
				+ "<br>";
		cuerpo = cuerpo + "<br><small>" + Utils.getMessage("mail.reserva.cuerpo.contacto.email") + "</small> "
				+ reserva.getInstalacion().getSede().getEmail();

		cuerpo = MessageFormat.format(cuerpo, reserva.getInstalacion().getNombre(), reserva.getFecha(),
				reserva.getHoraInicio(), reserva.getHoraFin());

		// 2. Generación del contenido del calendario (.ics) dinámico
		String tituloCita = "Reserva: " + reserva.getInstalacion().getNombre();
		String descripcionCita = "Tu reserva ha sido aprobada para la instalación "
				+ reserva.getInstalacion().getNombre();
		String idCita = "reserva-" + reserva.getId(); // Asegúrate de tener reserva.getId() o el identificador único

		String icsContent = generarIcsContentAprobacion(reserva.getFecha(), reserva.getHoraInicio(), reserva.getHoraFin(),
				tituloCita, descripcionCita, idCita);

		// 3. Envío del correo pasando también el contenido del calendario
		enviarMail(Arrays.asList(reserva.getUsuarioCreacion().getEmail()), asunto, cuerpo, icsContent);
	}

	private String generarIcsContentAprobacion(LocalDate fecha, LocalTime horaInicio, LocalTime horaFin, String titulo,
			String descripcion, String idReserva) {

		// Fusionamos la fecha única con la hora de inicio y fin para crear
		// LocalDateTime
		LocalDateTime inicio = fecha.atTime(horaInicio);
		LocalDateTime fin = fecha.atTime(horaFin);

		// Formato requerido por iCalendar (Ej: 20260706T140000)
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd'T'HHmmss");

		String fechaInicioStr = inicio.format(formatter);
		String fechaFinStr = fin.format(formatter);

		return "BEGIN:VCALENDAR\n" + "VERSION:2.0\n" + "PRODID:-//Tu Empresa//Mi Aplicacion//ES\n" + "METHOD:REQUEST\n"
				+ "BEGIN:VEVENT\n" + "UID:" + idReserva + "@tuapp.com\n" + "DTSTAMP:"
				+ LocalDateTime.now().format(formatter) + "\n" + "DTSTART:" + fechaInicioStr + "\n" + "DTEND:"
				+ fechaFinStr + "\n" + "SUMMARY:" + titulo + "\n" + "DESCRIPTION:" + descripcion + "\n" + "END:VEVENT\n"
				+ "END:VCALENDAR";
	}

}
