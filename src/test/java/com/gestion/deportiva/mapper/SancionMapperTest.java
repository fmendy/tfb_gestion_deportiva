package com.gestion.deportiva.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import com.gestion.deportiva.dto.ComboDTO;
import com.gestion.deportiva.dto.SancionDTO;
import com.gestion.deportiva.model.Empresa;
import com.gestion.deportiva.model.Instalacion;
import com.gestion.deportiva.model.Reserva;
import com.gestion.deportiva.model.ReservaEstado;
import com.gestion.deportiva.model.Sancion;
import com.gestion.deportiva.model.SancionTipo;
import com.gestion.deportiva.model.Sede;
import com.gestion.deportiva.model.Usuario;

class SancionMapperTest {

	private SancionMapper mapper;

	@BeforeEach
	void setUp() {
		mapper = new SancionMapper();
	}

	@Test
	void modelToDTO() {
		Empresa empresa = new Empresa();
		empresa.setNombre("Empresa Central");

		Sede sede = new Sede();
		sede.setNombre("Sede Norte");
		sede.setEmpresa(empresa);

		Instalacion instalacion = new Instalacion();
		instalacion.setNombre("Pista Central");
		instalacion.setSede(sede);

		ReservaEstado estado = new ReservaEstado();
		estado.setNombre("COMPLETADA");

		Reserva reserva = new Reserva();
		reserva.setId(10L);
		reserva.setFecha(LocalDate.of(2026, java.time.Month.AUGUST, 7));
		reserva.setHoraInicio(LocalTime.of(10, 0));
		reserva.setHoraFin(LocalTime.of(11, 0));
		reserva.setReservaEstado(estado);
		reserva.setInstalacion(instalacion);

		Usuario usuario = new Usuario();
		usuario.setId(20L);
		usuario.setNombre("Juan Pérez");

		SancionTipo tipo = new SancionTipo();
		tipo.setId(30L);
		tipo.setNombre("Leve");

		LocalDate fechaInicio = LocalDate.of(2026, java.time.Month.AUGUST, 7);
		LocalDate fechaFin = LocalDate.of(2026, java.time.Month.AUGUST, 14);

		Sancion model = new Sancion();
		model.setId(1L);
		model.setUuid("uuid-sancion-1");
		model.setFechaInicio(fechaInicio);
		model.setFechaFin(fechaFin);
		model.setDescripcion("No presentación");
		model.setReserva(reserva);
		model.setUsuario(usuario);
		model.setSancionTipo(tipo);

		SancionDTO dto = mapper.modelToDTO(model);

		assertThat(dto).isNotNull();
		assertThat(dto.getId()).isEqualTo(1L);
		assertThat(dto.getUuid()).isEqualTo("uuid-sancion-1");
		assertThat(dto.getFechaInicio()).isEqualTo(fechaInicio);
		assertThat(dto.getFechaFin()).isEqualTo(fechaFin);
		assertThat(dto.getDescripcion()).isEqualTo("No presentación");
		assertThat(dto.getReservaId()).isEqualTo(10L);
		assertThat(dto.getUsuarioId()).isEqualTo(20L);
		assertThat(dto.getUsuarioNombre()).isEqualTo("Juan Pérez");
		assertThat(dto.getSancionTipoId()).isEqualTo(30L);
		assertThat(dto.getSancionTipoNombre()).isEqualTo("Leve");
		assertThat(dto.getReservaReservaEstadoNombre()).isEqualTo("COMPLETADA");
		assertThat(dto.getReservaHoraInicio()).isEqualTo(LocalTime.of(10, 0));
		assertThat(dto.getReservaHoraFin()).isEqualTo(LocalTime.of(11, 0));
		assertThat(dto.getReservaFecha()).isEqualTo(LocalDate.of(2026, java.time.Month.AUGUST, 7));
		assertThat(dto.getReservaInstalacionNombre()).isEqualTo("Pista Central");
		assertThat(dto.getReservaInstalacionSedeNombre()).isEqualTo("Sede Norte");
		assertThat(dto.getReservaInstalacionSedeEmpresaNombre()).isEqualTo("Empresa Central");
	}

	@Test
	void listModelToListDTO() {
		Reserva reserva = new Reserva();
		reserva.setReservaEstado(new ReservaEstado());
		reserva.setInstalacion(new Instalacion());
		reserva.getInstalacion().setSede(new Sede());
		reserva.getInstalacion().getSede().setEmpresa(new Empresa());

		Usuario usuario = new Usuario();
		usuario.setNombre("Usuario");

		SancionTipo tipo = new SancionTipo();
		tipo.setNombre("Tipo");

		Sancion model = new Sancion();
		model.setId(1L);
		model.setReserva(reserva);
		model.setUsuario(usuario);
		model.setSancionTipo(tipo);

		List<SancionDTO> dtos = mapper.listModelToListDTO(List.of(model));

		assertThat(dtos).hasSize(1);
		assertThat(dtos.get(0).getId()).isEqualTo(1L);
	}

	@Test
	void pageToPageDTO() {
		Reserva reserva = new Reserva();
		reserva.setReservaEstado(new ReservaEstado());
		reserva.setInstalacion(new Instalacion());
		reserva.getInstalacion().setSede(new Sede());
		reserva.getInstalacion().getSede().setEmpresa(new Empresa());

		Usuario usuario = new Usuario();
		usuario.setNombre("Usuario");

		SancionTipo tipo = new SancionTipo();
		tipo.setNombre("Tipo");

		Sancion model = new Sancion();
		model.setId(1L);
		model.setReserva(reserva);
		model.setUsuario(usuario);
		model.setSancionTipo(tipo);

		PageRequest pageable = PageRequest.of(0, 10);
		Page<Sancion> pageModel = new PageImpl<>(List.of(model), pageable, 1);

		Page<SancionDTO> pageDto = mapper.pageToPageDTO(pageModel);

		assertThat(pageDto).isNotNull();
		assertThat(pageDto.getTotalElements()).isEqualTo(1);
		assertThat(pageDto.getContent()).hasSize(1);
		assertThat(pageDto.getContent().get(0).getId()).isEqualTo(1L);
	}

	@Test
	void dtoToModelCreandoNuevoSiModelEsNull() {
		LocalDate fechaInicio = LocalDate.of(2026, java.time.Month.AUGUST, 7);
		LocalDate fechaFin = LocalDate.of(2026, java.time.Month.AUGUST, 14);

		SancionDTO dto = new SancionDTO();
		dto.setId(5L);
		dto.setUuid("uuid-new");
		dto.setFechaInicio(fechaInicio);
		dto.setFechaFin(fechaFin);
		dto.setDescripcion("Test desc");
		dto.setReservaId(2L);
		dto.setUsuarioId(3L);
		dto.setSancionTipoId(4L);

		Sancion model = mapper.dtoToModel(dto, null);

		assertThat(model).isNotNull();
		assertThat(model.getId()).isEqualTo(5L);
		assertThat(model.getUuid()).isEqualTo("uuid-new");
		assertThat(model.getFechaInicio()).isEqualTo(fechaInicio);
		assertThat(model.getFechaFin()).isEqualTo(fechaFin);
		assertThat(model.getDescripcion()).isEqualTo("Test desc");
		assertThat(model.getReserva()).isNotNull();
		assertThat(model.getReserva().getId()).isEqualTo(2L);
		assertThat(model.getUsuario()).isNotNull();
		assertThat(model.getUsuario().getId()).isEqualTo(3L);
		assertThat(model.getSancionTipo()).isNotNull();
		assertThat(model.getSancionTipo().getId()).isEqualTo(4L);
	}

	@Test
	void dtoToModelActualizandoModelExistente() {
		Sancion model = new Sancion();
		model.setId(1L);
		model.setUuid("uuid-old");

		SancionDTO dto = new SancionDTO();
		dto.setId(2L);
		dto.setUuid("uuid-updated");
		dto.setReservaId(6L);
		dto.setUsuarioId(7L);
		dto.setSancionTipoId(8L);

		Sancion resultado = mapper.dtoToModel(dto, model);

		assertThat(resultado).isEqualTo(model);
		assertThat(resultado.getId()).isEqualTo(2L);
		assertThat(resultado.getUuid()).isEqualTo("uuid-updated");
		assertThat(resultado.getReserva().getId()).isEqualTo(6L);
		assertThat(resultado.getUsuario().getId()).isEqualTo(7L);
		assertThat(resultado.getSancionTipo().getId()).isEqualTo(8L);
	}

	@Test
	void dtoToModelConUuidVacioNoLoModifica() {
		Sancion model = new Sancion();
		model.setUuid("uuid-original");

		SancionDTO dto = new SancionDTO();
		dto.setUuid("");
		dto.setReservaId(1L);
		dto.setUsuarioId(1L);
		dto.setSancionTipoId(1L);

		Sancion resultado = mapper.dtoToModel(dto, model);

		assertThat(resultado.getUuid()).isEqualTo("uuid-original");
	}

	@Test
	void listModelToListComboDTO() {
		SancionTipo tipo = new SancionTipo();
		tipo.setNombre("Grave");

		Sancion model = new Sancion();
		model.setId(3L);
		model.setSancionTipo(tipo);

		List<ComboDTO> combos = mapper.listModelToListComboDTO(List.of(model));

		assertThat(combos).hasSize(1);
		assertThat(combos.get(0).getKey()).isEqualTo(3L);
		assertThat(combos.get(0).getValue()).isEqualTo("Grave");
	}

	@Test
	void dtoAndReservaToDTO() {
		Empresa empresa = new Empresa();
		empresa.setNombre("Empresa Test");

		Sede sede = new Sede();
		sede.setNombre("Sede Test");
		sede.setEmpresa(empresa);

		Instalacion instalacion = new Instalacion();
		instalacion.setNombre("Instalacion Test");
		instalacion.setSede(sede);

		ReservaEstado estado = new ReservaEstado();
		estado.setNombre("APROBADA");

		Usuario usuario = new Usuario();
		usuario.setNombre("Usuario Test");

		Reserva reserva = new Reserva();
		reserva.setId(15L);
		reserva.setUsuarioCreacion(usuario);
		reserva.setHoraInicio(LocalTime.of(9, 0));
		reserva.setHoraFin(LocalTime.of(10, 0));
		reserva.setFecha(LocalDate.of(2026, java.time.Month.AUGUST, 7));
		reserva.setInstalacion(instalacion);
		reserva.setReservaEstado(estado);

		SancionDTO dto = new SancionDTO();
		SancionDTO resultado = mapper.dtoAndReservaToDTO(dto, reserva);

		assertThat(resultado).isNotNull();
		assertThat(resultado.getReservaId()).isEqualTo(15L);
		assertThat(resultado.getUsuarioNombre()).isEqualTo("Usuario Test");
		assertThat(resultado.getReservaHoraInicio()).isEqualTo(LocalTime.of(9, 0));
		assertThat(resultado.getReservaHoraFin()).isEqualTo(LocalTime.of(10, 0));
		assertThat(resultado.getReservaFecha()).isEqualTo(LocalDate.of(2026, java.time.Month.AUGUST, 7));
		assertThat(resultado.getReservaInstalacionNombre()).isEqualTo("Instalacion Test");
		assertThat(resultado.getReservaInstalacionSedeNombre()).isEqualTo("Sede Test");
		assertThat(resultado.getReservaInstalacionSedeEmpresaNombre()).isEqualTo("Empresa Test");
		assertThat(resultado.getReservaReservaEstadoNombre()).isEqualTo("APROBADA");
	}
}