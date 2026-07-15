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
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.gestion.deportiva.model.Reserva;
import com.gestion.deportiva.model.Usuario;
import com.gestion.deportiva.service.MailService;
import com.gestion.deportiva.util.Utils;

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

	private static final DateTimeFormatter ICS_DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyyMMdd'T'HHmmss");

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

		if (StringUtils.hasText(icsContent)) {
			String method = icsContent.contains("METHOD:CANCEL") ? "CANCEL" : "REQUEST";

			MimeBodyPart htmlPart = new MimeBodyPart();
			htmlPart.setContent(cuerpo, "text/html; charset=UTF-8");

			MimeBodyPart calendarPart = new MimeBodyPart();
			calendarPart.setDataHandler(new DataHandler(
					new ByteArrayDataSource(icsContent, "text/calendar;method=" + method + ";charset=UTF-8")));

			calendarPart.setHeader("Content-Class", "urn:content-classes:calendarmessage");
			calendarPart.setHeader("Content-ID", "calendar_message");
			calendarPart.setHeader("Content-Type", "text/calendar; method=" + method + "; charset=UTF-8");
			calendarPart.setHeader("Content-Disposition", "inline");

			MimeMultipart multipart = new MimeMultipart("alternative");
			multipart.addBodyPart(htmlPart);
			multipart.addBodyPart(calendarPart);

			mensaje.setContent(multipart);
		} else {
			helper.setText(cuerpo, true);
		}

		mailSender.send(mensaje);
	}

	@Override
	public void mensajeAprobacionReserva(Reserva reserva) throws MessagingException, IOException {
		String asunto = Utils.getMessage("mail.reserva.asunto.aprobada");
		String cuerpo = construirCuerpoBaseReserva(reserva, "mail.reserva.cuerpo.aprobada");

		String tituloCita = Utils.getMessage("mail.reserva.ics") + ": " + reserva.getInstalacion().getNombre();
		String descripcionCita = Utils.getMessage("mail.reserva.ics.titulo.aprobada") + " "
				+ reserva.getInstalacion().getNombre();
		String idCita = "reserva-" + reserva.getUuid();

		String icsContent = generarIcsContent(reserva.getFecha(), reserva.getHoraInicio(), reserva.getHoraFin(),
				tituloCita, descripcionCita, idCita, "REQUEST", null, null);

		enviarMail(Arrays.asList(reserva.getUsuarioCreacion().getEmail()), asunto, cuerpo, icsContent);
	}

	@Override
	public void mensajeDenegacionReserva(Reserva reserva) throws MessagingException, IOException {
		String asunto = Utils.getMessage("mail.reserva.asunto.denegada");
		String cuerpo = construirCuerpoBaseReserva(reserva, "mail.reserva.cuerpo.denegada");
		// Añadir línea extra que tenías específica en denegación si aplica
		cuerpo = cuerpo + Utils.getMessage("mail.reserva.asunto.denegada") + "<br>";

		enviarMail(Arrays.asList(reserva.getUsuarioCreacion().getEmail()), asunto, cuerpo, null);
	}

	@Override
	public void mensajeCanceladaEmpresaReserva(Reserva reserva) throws MessagingException, IOException {
		String asunto = Utils.getMessage("mail.reserva.asunto.cancelada.empresa");
		String cuerpo = construirCuerpoBaseReserva(reserva, "mail.reserva.cuerpo.cancelada.empresa");

		String tituloCita = Utils.getMessage("mail.reserva.ics") + ": " + reserva.getInstalacion().getNombre();
		String descripcionCita = Utils.getMessage("mail.reserva.ics.titulo.cancelada") + " "
				+ reserva.getInstalacion().getNombre();
		String idCita = "reserva-" + reserva.getUuid();

		String icsContent = generarIcsContent(reserva.getFecha(), reserva.getHoraInicio(), reserva.getHoraFin(),
				tituloCita, descripcionCita, idCita, "CANCEL", "CANCELADO: ", "CANCELLED");

		enviarMail(Arrays.asList(reserva.getUsuarioCreacion().getEmail()), asunto, cuerpo, icsContent);
	}

	/**
	 * Método auxiliar unificado para evitar duplicar la estructura HTML del correo
	 * de reserva.
	 */
	private String construirCuerpoBaseReserva(Reserva reserva, String claveCuerpoMensaje) {
		var sede = reserva.getInstalacion().getSede();
		var muni = sede.getMunicipio();
		var prov = muni.getProvincia();

		StringBuilder sb = new StringBuilder();
		sb.append(Utils.getMessage(claveCuerpoMensaje)).append("<br>").append("<h1>")
				.append(Utils.getMessage("mail.reserva.cuerpo.datos")).append("</h1><br>").append("<b>")
				.append(Utils.getMessage("mail.reserva.cuerpo.localizacion")).append("</b>: ")
				.append(prov.getComunidadAutonoma().getNombre()).append(" - ").append(prov.getNombre()).append(" - ")
				.append(muni.getNombre()).append(" - ").append(sede.getDireccion()).append("<br>").append("<b>")
				.append(Utils.getMessage("mail.reserva.cuerpo.instalacion")).append("</b>: ")
				.append(reserva.getInstalacion().getNombre()).append(" (")
				.append(reserva.getInstalacion().getInstalacionTipo().getNombre()).append(")<br>").append("<b>")
				.append(Utils.getMessage("mail.reserva.cuerpo.fecha")).append("</b>: ").append(reserva.getFecha())
				.append("<br>").append("<b>").append(Utils.getMessage("mail.reserva.cuerpo.hora.inicio"))
				.append("</b>: ").append(reserva.getHoraInicio()).append("<br>").append("<b>")
				.append(Utils.getMessage("mail.reserva.cuerpo.hora.fin")).append("</b>: ").append(reserva.getHoraFin())
				.append("<br>").append("<br><small>").append(Utils.getMessage("mail.reserva.cuerpo.contacto.email"))
				.append("</small> ").append(sede.getEmail());

		return MessageFormat.format(sb.toString(), reserva.getInstalacion().getNombre(), reserva.getFecha(),
				reserva.getHoraInicio(), reserva.getHoraFin());
	}

	/**
	 * Método unificado y DRY para generar contenido iCalendar (.ics) tanto para
	 * solicitudes como cancelaciones.
	 */
	private String generarIcsContent(LocalDate fecha, LocalTime horaInicio, LocalTime horaFin, String titulo,
			String descripcion, String idReserva, String method, String summaryPrefix, String status) {

		LocalDateTime inicio = fecha.atTime(horaInicio);
		LocalDateTime fin = fecha.atTime(horaFin);

		String fechaInicioStr = inicio.format(ICS_DATE_FORMATTER);
		String fechaFinStr = fin.format(ICS_DATE_FORMATTER);
		String ahoraStr = LocalDateTime.now().format(ICS_DATE_FORMATTER);
		String summaryFinal = (summaryPrefix != null ? summaryPrefix : "") + titulo;

		StringBuilder ics = new StringBuilder();
		ics.append("BEGIN:VCALENDAR\n").append("VERSION:2.0\n").append("PRODID:-//Tu Empresa//Mi Aplicacion//ES\n")
				.append("METHOD:").append(method).append("\n").append("BEGIN:VEVENT\n").append("UID:").append(idReserva)
				.append("@tuapp.com\n").append("DTSTAMP:").append(ahoraStr).append("\n").append("DTSTART:")
				.append(fechaInicioStr).append("\n").append("DTEND:").append(fechaFinStr).append("\n")
				.append("SUMMARY:").append(summaryFinal).append("\n").append("DESCRIPTION:").append(descripcion)
				.append("\n");

		if (status != null) {
			ics.append("STATUS:").append(status).append("\n");
		}
		if ("CANCEL".equals(method)) {
			ics.append("SEQUENCE:1\n");
		}

		ics.append("END:VEVENT\n").append("END:VCALENDAR");

		return ics.toString();
	}

	@Override
	public void mensajeUsuarioPasswordOlvidada(Usuario usuario) throws MessagingException, IOException {
		String asunto = Utils.getMessage("mail.usuario.password.olvidada.asunto");

		StringBuilder sb = new StringBuilder();
		sb.append(Utils.getMessage("mail.usuario.password.olvidada.cuerpo")).append("<br>")
				.append("<a href='http://localhost:8080/publico/password/resetear?uuid=").append(usuario.getUuid())
				.append("'>").append(Utils.getMessage("boton.resetear")).append("</a><br>")
				.append(Utils.getMessage("mail.usuario.password.olvidada.cuerpo.dos"));
		enviarMail(Arrays.asList(usuario.getEmail()), asunto, sb.toString(), null);
	}

	@Override
	public void mensajeUsuarioNuevaPassword(Usuario usuario, String password) throws MessagingException, IOException {
		String asunto = Utils.getMessage("mail.usuario.password.nueva.asunto");

		StringBuilder sb = new StringBuilder();
		sb.append(Utils.getMessage("mail.usuario.password.olvidada.cuerpo")).append("<br>").append(password)
				.append("<br>").append("<a href='http://localhost:8080/login'>").append(Utils.getMessage("boton.iniciar.sesion"))
				.append("</a><br>");
		enviarMail(Arrays.asList(usuario.getEmail()), asunto, sb.toString(), null);
	}

}