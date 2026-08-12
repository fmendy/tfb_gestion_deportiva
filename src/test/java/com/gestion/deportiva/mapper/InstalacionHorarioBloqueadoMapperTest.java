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
import com.gestion.deportiva.dto.InstalacionHorarioBloqueadoDTO;
import com.gestion.deportiva.model.Empresa;
import com.gestion.deportiva.model.Instalacion;
import com.gestion.deportiva.model.InstalacionHorarioBloqueado;
import com.gestion.deportiva.model.Sede;

class InstalacionHorarioBloqueadoMapperTest {

	private InstalacionHorarioBloqueadoMapper mapper;

	@BeforeEach
	void setUp() {
		mapper = new InstalacionHorarioBloqueadoMapper();
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

		Instalacion instalacion = new Instalacion();
		instalacion.setId(3L);
		instalacion.setNombre("Pista de Pádel");
		instalacion.setSede(sede);

		LocalDate fecha = LocalDate.of(2026, java.time.Month.AUGUST, 7);
		LocalTime horaInicio = LocalTime.of(10, 0);
		LocalTime horaFin = LocalTime.of(12, 0);

		InstalacionHorarioBloqueado model = new InstalacionHorarioBloqueado();
		model.setId(10L);
		model.setUuid("uuid-bloqueo-10");
		model.setInstalacion(instalacion);
		model.setFecha(fecha);
		model.setHoraInicio(horaInicio);
		model.setHoraFin(horaFin);

		InstalacionHorarioBloqueadoDTO dto = mapper.modelToDTO(model);

		assertThat(dto).isNotNull();
		assertThat(dto.getId()).isEqualTo(10L);
		assertThat(dto.getUuid()).isEqualTo("uuid-bloqueo-10");
		assertThat(dto.getEmpresaId()).isEqualTo(1L);
		assertThat(dto.getEmpresaNombre()).isEqualTo("Empresa Central");
		assertThat(dto.getSedeId()).isEqualTo(2L);
		assertThat(dto.getSedeNombre()).isEqualTo("Sede Norte");
		assertThat(dto.getInstalacionId()).isEqualTo(3L);
		assertThat(dto.getInstalacionNombre()).isEqualTo("Pista de Pádel");
		assertThat(dto.getFecha()).isEqualTo(fecha);
		assertThat(dto.getHoraInicio()).isEqualTo(horaInicio);
		assertThat(dto.getHoraFin()).isEqualTo(horaFin);
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

		Instalacion instalacion = new Instalacion();
		instalacion.setId(1L);
		instalacion.setNombre("Instalacion");
		instalacion.setSede(sede);

		InstalacionHorarioBloqueado model = new InstalacionHorarioBloqueado();
		model.setId(1L);
		model.setInstalacion(instalacion);

		List<InstalacionHorarioBloqueadoDTO> dtos = mapper.listModelToListDTO(List.of(model));

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

		Instalacion instalacion = new Instalacion();
		instalacion.setId(1L);
		instalacion.setNombre("Instalacion");
		instalacion.setSede(sede);

		InstalacionHorarioBloqueado model = new InstalacionHorarioBloqueado();
		model.setId(1L);
		model.setInstalacion(instalacion);

		PageRequest pageable = PageRequest.of(0, 10);
		Page<InstalacionHorarioBloqueado> pageModel = new PageImpl<>(List.of(model), pageable, 1);

		Page<InstalacionHorarioBloqueadoDTO> pageDto = mapper.pageToPageDTO(pageModel);

		assertThat(pageDto).isNotNull();
		assertThat(pageDto.getTotalElements()).isEqualTo(1);
		assertThat(pageDto.getContent()).hasSize(1);
		assertThat(pageDto.getContent().get(0).getId()).isEqualTo(1L);
	}

	@Test
	void dtoToModelCreandoNuevoSiModelEsNull() {
		LocalDate fecha = LocalDate.of(2026, java.time.Month.AUGUST, 7);
		LocalTime horaInicio = LocalTime.of(8, 0);
		LocalTime horaFin = LocalTime.of(10, 0);

		InstalacionHorarioBloqueadoDTO dto = new InstalacionHorarioBloqueadoDTO();
		dto.setId(5L);
		dto.setUuid("uuid-new");
		dto.setInstalacionId(2L);
		dto.setFecha(fecha);
		dto.setHoraInicio(horaInicio);
		dto.setHoraFin(horaFin);

		InstalacionHorarioBloqueado model = mapper.dtoToModel(dto, null);

		assertThat(model).isNotNull();
		assertThat(model.getId()).isEqualTo(5L);
		assertThat(model.getUuid()).isEqualTo("uuid-new");
		assertThat(model.getInstalacion()).isNotNull();
		assertThat(model.getInstalacion().getId()).isEqualTo(2L);
		assertThat(model.getFecha()).isEqualTo(fecha);
		assertThat(model.getHoraInicio()).isEqualTo(horaInicio);
		assertThat(model.getHoraFin()).isEqualTo(horaFin);
	}

	@Test
	void dtoToModelActualizandoModelExistente() {
		InstalacionHorarioBloqueado model = new InstalacionHorarioBloqueado();
		model.setId(1L);
		model.setUuid("uuid-old");

		InstalacionHorarioBloqueadoDTO dto = new InstalacionHorarioBloqueadoDTO();
		dto.setId(2L);
		dto.setUuid("uuid-updated");
		dto.setInstalacionId(4L);

		InstalacionHorarioBloqueado resultado = mapper.dtoToModel(dto, model);

		assertThat(resultado).isEqualTo(model);
		assertThat(resultado.getId()).isEqualTo(2L);
		assertThat(resultado.getUuid()).isEqualTo("uuid-updated");
		assertThat(resultado.getInstalacion().getId()).isEqualTo(4L);
	}

	@Test
	void dtoToModelConUuidVacioNoLoModifica() {
		InstalacionHorarioBloqueado model = new InstalacionHorarioBloqueado();
		model.setUuid("uuid-original");

		InstalacionHorarioBloqueadoDTO dto = new InstalacionHorarioBloqueadoDTO();
		dto.setUuid("");
		dto.setInstalacionId(1L);

		InstalacionHorarioBloqueado resultado = mapper.dtoToModel(dto, model);

		assertThat(resultado.getUuid()).isEqualTo("uuid-original");
	}

	@Test
	void listModelToListComboDTO() {
		InstalacionHorarioBloqueado model = new InstalacionHorarioBloqueado();
		model.setId(1L);
		model.setHoraFin(LocalTime.of(14, 0));

		List<ComboDTO> combos = mapper.listModelToListComboDTO(List.of(model));

		assertThat(combos).hasSize(1);
	}
}