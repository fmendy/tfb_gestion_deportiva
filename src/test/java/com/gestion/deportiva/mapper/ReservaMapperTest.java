package com.gestion.deportiva.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import com.gestion.deportiva.dto.ComboDTO;
import com.gestion.deportiva.dto.ReservaDTO;
import com.gestion.deportiva.dto.ReservaListadoDTO;
import com.gestion.deportiva.dto.ReservaSolicitudDTO;
import com.gestion.deportiva.model.Empresa;
import com.gestion.deportiva.model.Instalacion;
import com.gestion.deportiva.model.InstalacionTipo;
import com.gestion.deportiva.model.Reserva;
import com.gestion.deportiva.model.ReservaEstado;
import com.gestion.deportiva.model.Sede;
import com.gestion.deportiva.model.Usuario;
import com.gestion.deportiva.util.Constantes;
import com.gestion.deportiva.util.SecurityUtil;

class ReservaMapperTest {

	private ReservaMapper mapper;

	@BeforeEach
	void setUp() {
		mapper = new ReservaMapper();
	}

	@Test
	void modelToDTO() {
		Empresa empresa = new Empresa();
		empresa.setId(1L);
		empresa.setNombre("Empresa Central");

		Sede sede = new Sede();
		sede.setId(2L);
		sede.setNombre("Sede Norte");
		sede.setEmpresa(empresa);

		InstalacionTipo tipo = new InstalacionTipo();
		tipo.setId(3L);
		tipo.setNombre("Pádel");

		Instalacion instalacion = new Instalacion();
		instalacion.setId(4L);
		instalacion.setNombre("Pista 1");
		instalacion.setSede(sede);
		instalacion.setInstalacionTipo(tipo);

		ReservaEstado estado = new ReservaEstado();
		estado.setId(5L);
		estado.setNombre("APROBADA");

		LocalDate fecha = LocalDate.of(2026, java.time.Month.AUGUST, 7);
		LocalTime horaInicio = LocalTime.of(10, 0);
		LocalTime horaFin = LocalTime.of(11, 30);

		Reserva model = new Reserva();
		model.setId(10L);
		model.setUuid("uuid-reserva-10");
		model.setInstalacion(instalacion);
		model.setFecha(fecha);
		model.setHoraInicio(horaInicio);
		model.setHoraFin(horaFin);
		model.setReservaEstado(estado);

		ReservaDTO dto = mapper.modelToDTO(model);

		assertThat(dto).isNotNull();
		assertThat(dto.getId()).isEqualTo(10L);
		assertThat(dto.getUuid()).isEqualTo("uuid-reserva-10");
		assertThat(dto.getInstalacionSedeEmpresaId()).isEqualTo(1L);
		assertThat(dto.getInstalacionSedeEmpresaNombre()).isEqualTo("Empresa Central");
		assertThat(dto.getInstalacionSedeId()).isEqualTo(2L);
		assertThat(dto.getInstalacionSedeNombre()).isEqualTo("Sede Norte");
		assertThat(dto.getInstalacionId()).isEqualTo(4L);
		assertThat(dto.getInstalacionNombre()).isEqualTo("Pista 1");
		assertThat(dto.getInstalacionInstalacionTipoId()).isEqualTo(3L);
		assertThat(dto.getInstalacionInstalacionTipoNombre()).isEqualTo("Pádel");
		assertThat(dto.getFecha()).isEqualTo(fecha);
		assertThat(dto.getHoraInicio()).isEqualTo(horaInicio);
		assertThat(dto.getHoraFin()).isEqualTo(horaFin);
		assertThat(dto.getReservaEstadoId()).isEqualTo(5L);
		assertThat(dto.getReservaEstadoNombre()).isEqualTo("APROBADA");
	}

	@Test
	void listModelToListDTO() {
		Empresa empresa = new Empresa();
		empresa.setId(1L);
		empresa.setNombre("Empresa");

		Sede sede = new Sede();
		sede.setId(1L);
		sede.setNombre("Sede");
		sede.setEmpresa(empresa);

		InstalacionTipo tipo = new InstalacionTipo();
		tipo.setId(1L);
		tipo.setNombre("Tipo");

		Instalacion instalacion = new Instalacion();
		instalacion.setId(1L);
		instalacion.setNombre("Instalacion");
		instalacion.setSede(sede);
		instalacion.setInstalacionTipo(tipo);

		ReservaEstado estado = new ReservaEstado();
		estado.setId(1L);
		estado.setNombre("PENDIENTE");

		Reserva model = new Reserva();
		model.setId(1L);
		model.setInstalacion(instalacion);
		model.setReservaEstado(estado);

		List<ReservaDTO> dtos = mapper.listModelToListDTO(List.of(model));

		assertThat(dtos).hasSize(1);
		assertThat(dtos.get(0).getId()).isEqualTo(1L);
	}

	@Test
	void pageToPageDTO() {
		Empresa empresa = new Empresa();
		empresa.setId(1L);
		empresa.setNombre("Empresa");

		Sede sede = new Sede();
		sede.setId(1L);
		sede.setNombre("Sede");
		sede.setEmpresa(empresa);

		InstalacionTipo tipo = new InstalacionTipo();
		tipo.setId(1L);
		tipo.setNombre("Tipo");

		Instalacion instalacion = new Instalacion();
		instalacion.setId(1L);
		instalacion.setNombre("Instalacion");
		instalacion.setSede(sede);
		instalacion.setInstalacionTipo(tipo);

		ReservaEstado estado = new ReservaEstado();
		estado.setId(1L);
		estado.setNombre("PENDIENTE");

		Reserva model = new Reserva();
		model.setId(1L);
		model.setInstalacion(instalacion);
		model.setReservaEstado(estado);

		PageRequest pageable = PageRequest.of(0, 10);
		Page<Reserva> pageModel = new PageImpl<>(List.of(model), pageable, 1);

		Page<ReservaDTO> pageDto = mapper.pageToPageDTO(pageModel);

		assertThat(pageDto).isNotNull();
		assertThat(pageDto.getTotalElements()).isEqualTo(1);
		assertThat(pageDto.getContent()).hasSize(1);
		assertThat(pageDto.getContent().get(0).getId()).isEqualTo(1L);
	}

	@Test
	void dtoToModelCreandoNuevoSiModelEsNull() {
		LocalDate fecha = LocalDate.of(2026, java.time.Month.AUGUST, 7);
		LocalTime horaInicio = LocalTime.of(9, 0);
		LocalTime horaFin = LocalTime.of(10, 0);

		ReservaDTO dto = new ReservaDTO();
		dto.setId(15L);
		dto.setUuid("uuid-new");
		dto.setFecha(fecha);
		dto.setHoraInicio(horaInicio);
		dto.setHoraFin(horaFin);
		dto.setInstalacionId(2L);
		dto.setReservaEstadoId(3L);

		Reserva model = mapper.dtoToModel(dto, null);

		assertThat(model).isNotNull();
		assertThat(model.getId()).isEqualTo(15L);
		assertThat(model.getUuid()).isEqualTo("uuid-new");
		assertThat(model.getFecha()).isEqualTo(fecha);
		assertThat(model.getHoraInicio()).isEqualTo(horaInicio);
		assertThat(model.getHoraFin()).isEqualTo(horaFin);
		assertThat(model.getInstalacion()).isNotNull();
		assertThat(model.getInstalacion().getId()).isEqualTo(2L);
		assertThat(model.getReservaEstado()).isNotNull();
		assertThat(model.getReservaEstado().getId()).isEqualTo(3L);
	}

	@Test
	void dtoToModelActualizandoModelExistente() {
		Reserva model = new Reserva();
		model.setId(1L);
		model.setUuid("uuid-old");

		ReservaDTO dto = new ReservaDTO();
		dto.setId(2L);
		dto.setUuid("uuid-updated");
		dto.setInstalacionId(4L);
		dto.setReservaEstadoId(6L);

		Reserva resultado = mapper.dtoToModel(dto, model);

		assertThat(resultado).isEqualTo(model);
		assertThat(resultado.getId()).isEqualTo(2L);
		assertThat(resultado.getUuid()).isEqualTo("uuid-updated");
		assertThat(resultado.getInstalacion().getId()).isEqualTo(4L);
		assertThat(resultado.getReservaEstado().getId()).isEqualTo(6L);
	}

	@Test
	void dtoToModelConUuidVacioNoLoModifica() {
		Reserva model = new Reserva();
		model.setUuid("uuid-original");

		ReservaDTO dto = new ReservaDTO();
		dto.setUuid("");

		Reserva resultado = mapper.dtoToModel(dto, model);

		assertThat(resultado.getUuid()).isEqualTo("uuid-original");
	}

	@Test
	void listModelToListComboDTOretornaNull() {
		List<ComboDTO> combos = mapper.listModelToListComboDTO(List.of());
		assertThat(combos).isNull();
	}

	@Test
	void instalacionModelToReservaInstalacionDTO() {
		Empresa empresa = new Empresa();
		empresa.setNombre("Empresa Test");

		Sede sede = new Sede();
		sede.setNombre("Sede Test");
		sede.setEmpresa(empresa);

		InstalacionTipo tipo = new InstalacionTipo();
		tipo.setNombre("Tipo Test");

		Instalacion instalacion = new Instalacion();
		instalacion.setId(7L);
		instalacion.setNombre("Instalacion Test");
		instalacion.setSede(sede);
		instalacion.setInstalacionTipo(tipo);

		ReservaSolicitudDTO entradaDTO = new ReservaSolicitudDTO();
		entradaDTO.setDuracion(60L);
		entradaDTO.setFecha(LocalDate.of(2026, java.time.Month.AUGUST, 7));
		entradaDTO.setHora(LocalTime.of(12, 0));

		ReservaSolicitudDTO resultado = mapper.instalacionModelToReservaInstalacionDTO(instalacion, entradaDTO);

		assertThat(resultado).isNotNull();
		assertThat(resultado.getDuracion()).isEqualTo(60);
		assertThat(resultado.getFecha()).isEqualTo(LocalDate.of(2026, java.time.Month.AUGUST, 7));
		assertThat(resultado.getHora()).isEqualTo(LocalTime.of(12, 0));
		assertThat(resultado.getHoraFin()).isEqualTo(LocalTime.of(13, 0));
		assertThat(resultado.getInstalacionId()).isEqualTo(7L);
		assertThat(resultado.getInstalacionNombre()).isEqualTo("Instalacion Test");
		assertThat(resultado.getInstalacionSedeNombre()).isEqualTo("Sede Test");
		assertThat(resultado.getInstalacionSedeEmpresaNombre()).isEqualTo("Empresa Test");
		assertThat(resultado.getInstalacionInstalacionTipoNombre()).isEqualTo("Tipo Test");
	}

	@Test
	void modelToReservaListadoDTOYListYPagh() {
		try (MockedStatic<SecurityUtil> securityUtilMock = Mockito.mockStatic(SecurityUtil.class)) {
			securityUtilMock.when(SecurityUtil::getCurrentUserId).thenReturn(100L);
			securityUtilMock.when(() -> SecurityUtil.hasAnyAuthority(Mockito.any())).thenReturn(true);

			Empresa empresa = new Empresa();
			empresa.setId(1L);
			empresa.setNombre("Empresa");

			Sede sede = new Sede();
			sede.setId(1L);
			sede.setNombre("Sede");
			sede.setEmpresa(empresa);

			InstalacionTipo tipo = new InstalacionTipo();
			tipo.setId(1L);
			tipo.setNombre("Tipo");

			Instalacion instalacion = new Instalacion();
			instalacion.setId(1L);
			instalacion.setNombre("Instalacion");
			instalacion.setSede(sede);
			instalacion.setInstalacionTipo(tipo);

			Usuario usuario = new Usuario();
			usuario.setId(100L);
			usuario.setNombre("Usuario Test");

			ReservaEstado estadoPendiente = new ReservaEstado();
			estadoPendiente.setId(1L);
			estadoPendiente.setNombre(Constantes.ReservaEstado.PENDIENTE);

			Reserva model = new Reserva();
			model.setId(1L);
			model.setUuid("uuid-list");
			model.setInstalacion(instalacion);
			model.setUsuarioCreacion(usuario);
			model.setReservaEstado(estadoPendiente);
			model.setFecha(LocalDate.now().plusDays(5));
			model.setHoraInicio(LocalTime.of(10, 0));
			model.setHoraFin(LocalTime.of(11, 0));

			ReservaListadoDTO listadoDTO = mapper.modelToReservaListadoDTO(model);

			assertThat(listadoDTO).isNotNull();
			assertThat(listadoDTO.getId()).isEqualTo(1L);
			assertThat(listadoDTO.getUsuarioId()).isEqualTo(100L);
			assertThat(listadoDTO.isMostrarEliminar()).isTrue();
			assertThat(listadoDTO.isMostrarAprobar()).isFalse();
			assertThat(listadoDTO.isMostrarDenegar()).isFalse();
			assertThat(listadoDTO.getReservaEstadoCss()).contains("warning");

			List<ReservaListadoDTO> lista = mapper.listModelToListReservaListadoDTO(List.of(model));
			assertThat(lista).hasSize(1);

			PageRequest pageable = PageRequest.of(0, 10);
			Page<Reserva> pageModel = new PageImpl<>(List.of(model), pageable, 1);
			Page<ReservaListadoDTO> pageListado = mapper.pageToPageReservaListadoDTO(pageModel);
			assertThat(pageListado).isNotNull();
			assertThat(pageListado.getTotalElements()).isEqualTo(1);
		}
	}

	@Test
	void modelToReservaListadoDTOEstadosDiferentesCss() {
		try (MockedStatic<SecurityUtil> securityUtilMock = Mockito.mockStatic(SecurityUtil.class)) {
			securityUtilMock.when(SecurityUtil::getCurrentUserId).thenReturn(1L);
			securityUtilMock.when(() -> SecurityUtil.hasAnyAuthority(Mockito.any())).thenReturn(false);

			Empresa empresa = new Empresa();
			empresa.setId(1L);
			empresa.setNombre("Empresa");

			Sede sede = new Sede();
			sede.setId(1L);
			sede.setNombre("Sede");
			sede.setEmpresa(empresa);

			InstalacionTipo tipo = new InstalacionTipo();
			tipo.setId(1L);
			tipo.setNombre("Tipo");

			Instalacion instalacion = new Instalacion();
			instalacion.setId(1L);
			instalacion.setNombre("Instalacion");
			instalacion.setSede(sede);
			instalacion.setInstalacionTipo(tipo);

			Usuario usuario = new Usuario();
			usuario.setId(1L);
			usuario.setNombre("Usuario");

			// Test Aprobada
			ReservaEstado estadoAprobada = new ReservaEstado();
			estadoAprobada.setNombre(Constantes.ReservaEstado.APROBADA);
			Reserva m1 = new Reserva();
			m1.setInstalacion(instalacion);
			m1.setUsuarioCreacion(usuario);
			m1.setReservaEstado(estadoAprobada);
			assertThat(mapper.modelToReservaListadoDTO(m1).getReservaEstadoCss()).contains("success");

			// Test Cancelada por usuario
			ReservaEstado estadoCancelada = new ReservaEstado();
			estadoCancelada.setNombre(Constantes.ReservaEstado.CANCELADA_POR_USUARIO);
			Reserva m2 = new Reserva();
			m2.setInstalacion(instalacion);
			m2.setUsuarioCreacion(usuario);
			m2.setReservaEstado(estadoCancelada);
			assertThat(mapper.modelToReservaListadoDTO(m2).getReservaEstadoCss()).contains("danger");

			// Test Completada
			ReservaEstado estadoCompletada = new ReservaEstado();
			estadoCompletada.setNombre(Constantes.ReservaEstado.COMPLETADA);
			Reserva m3 = new Reserva();
			m3.setInstalacion(instalacion);
			m3.setUsuarioCreacion(usuario);
			m3.setReservaEstado(estadoCompletada);
			assertThat(mapper.modelToReservaListadoDTO(m3).getReservaEstadoCss()).contains("primary");

			// Test Incompletada
			ReservaEstado estadoIncompletada = new ReservaEstado();
			estadoIncompletada.setNombre(Constantes.ReservaEstado.INCOMPLETADA);
			Reserva m4 = new Reserva();
			m4.setInstalacion(instalacion);
			m4.setUsuarioCreacion(usuario);
			m4.setReservaEstado(estadoIncompletada);
			assertThat(mapper.modelToReservaListadoDTO(m4).getReservaEstadoCss()).contains("dark");

			// Test Denegada
			ReservaEstado estadoDenegada = new ReservaEstado();
			estadoDenegada.setNombre(Constantes.ReservaEstado.DENEGADA);
			Reserva m5 = new Reserva();
			m5.setInstalacion(instalacion);
			m5.setUsuarioCreacion(usuario);
			m5.setReservaEstado(estadoDenegada);
			assertThat(mapper.modelToReservaListadoDTO(m5).getReservaEstadoCss()).contains("secondary");

			// Test Default
			ReservaEstado estadoOtro = new ReservaEstado();
			estadoOtro.setNombre("OTRO_ESTADO");
			Reserva m6 = new Reserva();
			m6.setInstalacion(instalacion);
			m6.setUsuarioCreacion(usuario);
			m6.setReservaEstado(estadoOtro);
			assertThat(mapper.modelToReservaListadoDTO(m6).getReservaEstadoCss()).contains("light");
		}
	}
}