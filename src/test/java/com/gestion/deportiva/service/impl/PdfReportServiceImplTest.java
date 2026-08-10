package com.gestion.deportiva.service.impl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.hibernate.envers.AuditReader;
import org.hibernate.envers.AuditReaderFactory;
import org.hibernate.envers.query.AuditQuery;
import org.hibernate.envers.query.AuditQueryCreator;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockHttpServletResponse;

import com.gestion.deportiva.model.Empresa;
import com.gestion.deportiva.model.Instalacion;
import com.gestion.deportiva.model.Reserva;
import com.gestion.deportiva.model.ReservaEstado;
import com.gestion.deportiva.model.Sede;
import com.gestion.deportiva.model.Usuario;
import com.gestion.deportiva.repository.ReservaEstadoRepository;
import com.gestion.deportiva.repository.ReservaRepository;
import com.gestion.deportiva.repository.UsuarioRepository;
import com.gestion.deportiva.util.Utils;

import jakarta.persistence.EntityManager;

@ExtendWith(MockitoExtension.class)
class PdfReportServiceImplTest {

	@Mock
	private UsuarioRepository usuarioRepository;

	@Mock
	private ReservaRepository reservaRepository;

	@Mock
	private ReservaEstadoRepository reservaEstadoRepository;

	@Mock
	private EntityManager entityManager;

	@Mock
	private AuditReader auditReader;

	@Mock
	private AuditQueryCreator auditQueryCreator;

	@Mock
	private AuditQuery auditQuery;

	@InjectMocks
	private PdfReportServiceImpl pdfReportService;

	private MockedStatic<AuditReaderFactory> auditReaderFactoryMocked;
	private MockedStatic<Utils> utilsMocked;

	@BeforeEach
	void setUp() {
		auditReaderFactoryMocked = mockStatic(AuditReaderFactory.class);
		utilsMocked = mockStatic(Utils.class);

		utilsMocked.when(() -> Utils.getMessage(any())).thenAnswer(invocation -> invocation.getArgument(0));
	}

	@AfterEach
	void tearDown() {
		if (auditReaderFactoryMocked != null) {
			auditReaderFactoryMocked.close();
		}
		if (utilsMocked != null) {
			utilsMocked.close();
		}
	}

	@Test
	void exportarDatosArcoUsuarioPdfExitoTest() throws IOException {
		Long usuarioId = 1L;
		MockHttpServletResponse response = new MockHttpServletResponse();

		Usuario usuario = new Usuario();
		usuario.setId(usuarioId);
		usuario.setNombre("Test User");
		usuario.setEmail("test@test.com");
		usuario.setFechaCreacion(LocalDateTime.now());

		when(usuarioRepository.findByActivoTrueAndId(usuarioId)).thenReturn(usuario);
		auditReaderFactoryMocked.when(() -> AuditReaderFactory.get(entityManager)).thenReturn(auditReader);

		when(auditReader.createQuery()).thenReturn(auditQueryCreator);
		when(auditQueryCreator.forRevisionsOfEntity(any(Class.class), anyBoolean(), anyBoolean()))
				.thenReturn(auditQuery);
		when(auditQuery.add(any())).thenReturn(auditQuery);

		Usuario usuarioRev = new Usuario();
		usuarioRev.setNombre("Test User Rev");
		usuarioRev.setEmail("test@test.com");
		usuarioRev.setFechaModificacion(LocalDateTime.now());
		Object[] revUsuarioRow = new Object[] { usuarioRev, null, null };

		Reserva reserva = new Reserva();
		reserva.setId(10L);
		reserva.setFecha(LocalDate.now());
		reserva.setHoraInicio(LocalTime.of(10, 0));
		reserva.setHoraFin(LocalTime.of(11, 0));

		Empresa empresa = new Empresa();
		empresa.setNombre("Empresa S.L.");
		Sede sede = new Sede();
		sede.setNombre("Sede Central");
		sede.setEmpresa(empresa);
		Instalacion instalacion = new Instalacion();
		instalacion.setNombre("Pista 1");
		instalacion.setSede(sede);
		reserva.setInstalacion(instalacion);

		ReservaEstado estado = new ReservaEstado();
		estado.setId(1L);
		estado.setNombre("Confirmada");
		reserva.setReservaEstado(estado);

		Object[] revReservaRow = new Object[] { reserva, null, null };

		// Configurar las llamadas secuenciales:
		// 1ª llamada: Histórico de Usuario
		// 2ª llamada: Histórico de Reserva
		when(auditQuery.getResultList()).thenReturn(java.util.Collections.singletonList(revUsuarioRow))
				.thenReturn(java.util.Collections.singletonList(revReservaRow));

		when(reservaRepository.findByActivoTrueAndUsuarioCreacionId(usuarioId)).thenReturn(List.of(reserva));
		when(reservaEstadoRepository.findById(1L)).thenReturn(Optional.of(estado));

		pdfReportService.exportarDatosArcoUsuarioPdf(usuarioId, response);

		assertThat(response.getContentType()).isEqualTo("application/pdf");
		assertThat(response.getHeader("Content-Disposition"))
				.contains("attachment; filename=informe-datos-personales-arco.pdf");
		verify(usuarioRepository).findByActivoTrueAndId(usuarioId);
	}

	@Test
	void exportarDatosArcoUsuarioPdfSinReservasTest() throws IOException {
		Long usuarioId = 1L;
		MockHttpServletResponse response = new MockHttpServletResponse();

		Usuario usuario = new Usuario();
		usuario.setId(usuarioId);
		usuario.setNombre("Test User");

		when(usuarioRepository.findByActivoTrueAndId(usuarioId)).thenReturn(usuario);
		auditReaderFactoryMocked.when(() -> AuditReaderFactory.get(entityManager)).thenReturn(auditReader);

		when(auditReader.createQuery()).thenReturn(auditQueryCreator);
		when(auditQueryCreator.forRevisionsOfEntity(any(Class.class), anyBoolean(), anyBoolean()))
				.thenReturn(auditQuery);
		when(auditQuery.add(any())).thenReturn(auditQuery);
		when(auditQuery.getResultList()).thenReturn(Collections.emptyList());

		when(reservaRepository.findByActivoTrueAndUsuarioCreacionId(usuarioId)).thenReturn(Collections.emptyList());

		pdfReportService.exportarDatosArcoUsuarioPdf(usuarioId, response);

		assertThat(response.getContentType()).isEqualTo("application/pdf");
		verify(reservaRepository).findByActivoTrueAndUsuarioCreacionId(usuarioId);
	}

	@Test
	void exportarDatosArcoUsuarioPdfUsuarioNuloTest() throws IOException {
		Long usuarioId = 99L;
		MockHttpServletResponse response = new MockHttpServletResponse();

		when(usuarioRepository.findByActivoTrueAndId(usuarioId)).thenReturn(null);
		auditReaderFactoryMocked.when(() -> AuditReaderFactory.get(entityManager)).thenReturn(auditReader);

		when(auditReader.createQuery()).thenReturn(auditQueryCreator);
		when(auditQueryCreator.forRevisionsOfEntity(any(Class.class), anyBoolean(), anyBoolean()))
				.thenReturn(auditQuery);
		when(auditQuery.add(any())).thenReturn(auditQuery);
		when(auditQuery.getResultList()).thenReturn(Collections.emptyList());

		when(reservaRepository.findByActivoTrueAndUsuarioCreacionId(usuarioId)).thenReturn(Collections.emptyList());

		pdfReportService.exportarDatosArcoUsuarioPdf(usuarioId, response);

		assertThat(response.getContentType()).isEqualTo("application/pdf");
	}
}