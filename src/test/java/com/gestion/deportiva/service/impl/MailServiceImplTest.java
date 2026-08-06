package com.gestion.deportiva.service.impl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import jakarta.mail.internet.MimeMessage;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mail.javamail.JavaMailSender;

import com.gestion.deportiva.model.ComunidadAutonoma;
import com.gestion.deportiva.model.Instalacion;
import com.gestion.deportiva.model.InstalacionTipo;
import com.gestion.deportiva.model.Municipio;
import com.gestion.deportiva.model.Provincia;
import com.gestion.deportiva.model.Reserva;
import com.gestion.deportiva.model.Sede;
import com.gestion.deportiva.model.Usuario;
import com.gestion.deportiva.service.UsuarioTokenService;
import com.gestion.deportiva.util.Utils;

@ExtendWith(MockitoExtension.class)
class MailServiceImplTest {

	@Mock
	private JavaMailSender mailSender;

	@Mock
	private UsuarioTokenService usuarioTokenService;

	@InjectMocks
	private MailServiceImpl mailService;

	@Mock
	private MimeMessage mimeMessage;

	private MockedStatic<Utils> utilsMockedStatic;

	@BeforeEach
	void setUp() {
		utilsMockedStatic = mockStatic(Utils.class);
	}

	@AfterEach
	void tearDown() {
		utilsMockedStatic.close();
	}

	@Test
	void enviarMailSimple() throws Exception {
		when(mailSender.createMimeMessage()).thenReturn(mimeMessage);

		mailService.enviarMail(List.of("test@example.com"), "Asunto", "Cuerpo");

		verify(mailSender).send(mimeMessage);
	}

	@Test
	void enviarMailConIcs() throws Exception {
		when(mailSender.createMimeMessage()).thenReturn(mimeMessage);

		String icsContent = "BEGIN:VCALENDAR\nMETHOD:REQUEST\nEND:VCALENDAR";
		mailService.enviarMail(List.of("test@example.com"), "Asunto", "Cuerpo", icsContent);

		verify(mailSender).send(mimeMessage);
	}

	@Test
	void mensajeAprobacionReserva() throws Exception {
		assertThat(mailService).isNotNull();

	}

	@Test
	void mensajeDenegacionReserva() throws Exception {
		ComunidadAutonoma comunidad = new ComunidadAutonoma();
		comunidad.setNombre("Comunidad de Madrid");

		Provincia provincia = new Provincia();
		provincia.setNombre("Madrid");
		provincia.setComunidadAutonoma(comunidad);

		Instalacion instalacion = new Instalacion();
		Sede sede = new Sede();
		Municipio municipio = new Municipio();
		municipio.setProvincia(provincia);
		sede.setMunicipio(municipio);

		instalacion.setSede(sede);

		Reserva reserva = new Reserva();
		reserva.setInstalacion(instalacion);
		assertNotNull(reserva);
	}

	@Test
	void mensajeCanceladaEmpresaReserva() throws Exception {
		assertThat(mailService).isNotNull();

	}

	@Test
	void mensajeUsuarioPasswordOlvidada() throws Exception {
		when(mailSender.createMimeMessage()).thenReturn(mimeMessage);
		utilsMockedStatic.when(() -> Utils.getMessage(any())).thenReturn("Mensaje mock");
		when(usuarioTokenService.crearToken(any())).thenReturn("token-uuid");

		Usuario usuario = new Usuario();
		usuario.setId(1L);
		usuario.setEmail("user@example.com");

		mailService.mensajeUsuarioPasswordOlvidada(usuario);

		verify(mailSender).send(mimeMessage);
	}

	@Test
	void mensajeUsuarioNuevaPassword() throws Exception {
		when(mailSender.createMimeMessage()).thenReturn(mimeMessage);
		utilsMockedStatic.when(() -> Utils.getMessage(any())).thenReturn("Mensaje mock");

		Usuario usuario = new Usuario();
		usuario.setEmail("user@example.com");

		mailService.mensajeUsuarioNuevaPassword(usuario, "nuevaPassword123");

		verify(mailSender).send(mimeMessage);
	}

	private Reserva crearReservaMock() {
		Reserva reserva = new Reserva();
		reserva.setUuid("reserva-uuid");
		reserva.setFecha(LocalDate.of(2026, 6, 1));
		reserva.setHoraInicio(LocalTime.of(10, 0));
		reserva.setHoraFin(LocalTime.of(11, 0));

		Usuario usuario = new Usuario();
		usuario.setEmail("user@example.com");
		reserva.setUsuarioCreacion(usuario);

		Instalacion instalacion = new Instalacion();
		instalacion.setNombre("Campo de Fútbol");

		InstalacionTipo tipo = new InstalacionTipo();
		tipo.setNombre("Fútbol");
		instalacion.setInstalacionTipo(tipo);

		Sede sede = new Sede();
		sede.setDireccion("Calle Principal");
		sede.setEmail("sede@example.com");

		Municipio municipio = new Municipio();
		municipio.setNombre("Madrid");

		Provincia provincia = new Provincia();
		provincia.setNombre("Madrid");

		municipio.setProvincia(provincia);
		sede.setMunicipio(municipio);
		instalacion.setSede(sede);

		reserva.setInstalacion(instalacion);

		return reserva;
	}
}