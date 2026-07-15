package com.gestion.deportiva.service;

import java.io.IOException;
import java.util.List;

import com.gestion.deportiva.model.Reserva;
import com.gestion.deportiva.model.Usuario;

import jakarta.mail.MessagingException;

public interface MailService {

	void enviarMail(List<String> destinatarios, String asunto, String cuerpo) throws MessagingException, IOException;

	void enviarMail(List<String> destinatarios, String asunto, String cuerpo, String icsContent)
			throws MessagingException, IOException;

	void mensajeAprobacionReserva(Reserva reserva) throws MessagingException, IOException;

	void mensajeDenegacionReserva(Reserva reserva) throws MessagingException, IOException;

	void mensajeCanceladaEmpresaReserva(Reserva reserva) throws MessagingException, IOException;

	void mensajeUsuarioPasswordOlvidada(Usuario usuario) throws MessagingException, IOException;

	void mensajeUsuarioNuevaPassword(Usuario usuario, String password) throws MessagingException, IOException;

}
